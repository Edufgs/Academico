package persistencia;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import vo.AlunoVO;
import vo.CursoVO;
import vo.DisciplinaVO;
import vo.EnumSexo;
import vo.EnumUF;

public class AlunoDAO {

    private PreparedStatement comandoIncluir;
    private PreparedStatement comandoExcluir;
    private PreparedStatement comandoAlterar;
    private PreparedStatement comandoBuscarMatricula;
    private PreparedStatement comandoBuscarNome;
    private PreparedStatement comandoRecuperar;
    private PreparedStatement comandoRecuperarPorCurso;
    private PreparedStatement comandoIncluirDisciplina;
    private PreparedStatement comandoExcluirAlunoDisciplina;
    private PreparedStatement comandoRecuperarRegistro;
    private PreparedStatement comandoExcluirDisciplinaAluno;
    private ConexaoBD conexao;

    public AlunoDAO(ConexaoBD conexao) throws PersistenciaException {
        this.conexao = conexao;
        try {
            this.comandoIncluir = conexao.getConexao().prepareStatement("INSERT INTO aluno(nome,nomemae,nomepai,sexo,logradouro,numero,bairro,cidade,uf,curso) VALUES(?,?,?,?,?,?,?,?,?,?)");
            this.comandoAlterar = conexao.getConexao().prepareStatement("UPDATE aluno SET nome=?, nomemae=?, nomepai=?, sexo=?, logradouro=?, numero=?, bairro=?, cidade=?, uf=?, curso=? WHERE matricula=?");
            this.comandoExcluir = conexao.getConexao().prepareStatement("DELETE FROM aluno WHERE matricula=?");
            this.comandoBuscarMatricula = conexao.getConexao().prepareStatement("SELECT * FROM aluno WHERE matricula=?");
            this.comandoBuscarNome = conexao.getConexao().prepareStatement("SELECT * FROM aluno WHERE UPPER(nome) LIKE ?");
            this.comandoRecuperar = conexao.getConexao().prepareStatement("SELECT * FROM aluno ORDER BY nome");
            this.comandoRecuperarPorCurso = conexao.getConexao().prepareStatement("SELECT * FROM aluno WHERE curso=?");
            this.comandoIncluirDisciplina = conexao.getConexao().prepareStatement("INSERT INTO registraalunodisciplina(disciplina, aluno) VALUES(?,?)");
            this.comandoExcluirAlunoDisciplina = conexao.getConexao().prepareStatement("DELETE FROM registraalunodisciplina WHERE aluno=?");
            this.comandoExcluirDisciplinaAluno = conexao.getConexao().prepareStatement("DELETE FROM registraalunodisciplina WHERE disciplina=?");
            this.comandoRecuperarRegistro = conexao.getConexao().prepareStatement("SELECT * FROM registraalunodisciplina ORDER BY codigo");
        } catch (SQLException e) {
            throw new PersistenciaException("ERRO AlunoDAO" + e.getMessage());
        }
    }

    public int incluir(AlunoVO alunoVO) throws PersistenciaException {
        int retorno = 0;
        try {
            comandoIncluir.setString(1, alunoVO.getNome());
            comandoIncluir.setString(2, alunoVO.getNomeMae());
            comandoIncluir.setString(3, alunoVO.getNomePai());
            comandoIncluir.setInt(4, alunoVO.getSexo().ordinal());
            comandoIncluir.setString(5, alunoVO.getEndereco().getLogradouro());
            comandoIncluir.setInt(6, alunoVO.getEndereco().getNumero());
            comandoIncluir.setString(7, alunoVO.getEndereco().getBairro());
            comandoIncluir.setString(8, alunoVO.getEndereco().getCidade());
            comandoIncluir.setString(9, alunoVO.getEndereco().getUf().name());
            comandoIncluir.setInt(10, alunoVO.getCurso().getCodigo());
            retorno = comandoIncluir.executeUpdate();           
        } catch (SQLException ex) {            
            throw new PersistenciaException("Erro ao incluir novo aluno − " + ex.getMessage());       
        }
        return retorno;
    }
    
    public int cadastrarAlunoDisciplina(AlunoVO alunoVO) throws PersistenciaException{
        int retorno = 0;
        try {
            conexao.getConexao().setAutoCommit(false); 
            int i = 0;
            while(i < alunoVO.getDisciplinas().size()){
                comandoIncluirDisciplina.setInt(1, alunoVO.getDisciplinas().get(i).getCodigo());
                comandoIncluirDisciplina.setInt(2, alunoVO.getMatricula());
                retorno +=comandoIncluirDisciplina.executeUpdate();
                i++;
            }                
            conexao.getConexao().commit(); //efetiva as operações
            conexao.getConexao().setAutoCommit(true);     
        } catch (SQLException ex) {
            try {
                conexao.getConexao().rollback(); //Cancela todas as operações feitas
                System.out.println("Rollback executado");
                conexao.getConexao().setAutoCommit(true);
            } catch (SQLException ex1) {
                System.out.println("Erro no rollback: " + ex1.toString());
            }
            throw new PersistenciaException("Erro ao incluir registro de aluno em disciplina − " + ex.getMessage());                 
        }
                                       
        return retorno;
    }

    public int alterar(AlunoVO alunoVO) throws PersistenciaException {
        int retorno = 0;
        try {
            comandoAlterar.setString(1, alunoVO.getNome());
            comandoAlterar.setString(2, alunoVO.getNomeMae());
            comandoAlterar.setString(3, alunoVO.getNomePai());
            comandoAlterar.setInt(4, alunoVO.getSexo().ordinal());
            comandoAlterar.setString(5, alunoVO.getEndereco().getLogradouro());
            comandoAlterar.setInt(6, alunoVO.getEndereco().getNumero());
            comandoAlterar.setString(7, alunoVO.getEndereco().getBairro());
            comandoAlterar.setString(8, alunoVO.getEndereco().getCidade());
            comandoAlterar.setString(9, alunoVO.getEndereco().getUf().name());
            comandoAlterar.setInt(10, alunoVO.getCurso().getCodigo());
            comandoAlterar.setInt(11, alunoVO.getMatricula());
            retorno = comandoAlterar.executeUpdate();
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao alterar o aluno " + e.getMessage());
        }
        return retorno;
    }

    public int excluir(int matricula) throws PersistenciaException {
        int retorno = 0;
        try {     
            conexao.getConexao().setAutoCommit(false);
            comandoExcluir.setInt(1, matricula);
            retorno += comandoExcluir.executeUpdate();
            retorno += excluirRegistroAluno(matricula);
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao excluir aluno" + e);
        }
        return retorno;
    }
    
    public int excluirRegistroAluno(int matricula) throws PersistenciaException{
        int retorno = 0;
        List<DisciplinaVO> disciplinaDelete = obterDisciplinasAluno(matricula);
        try {
            int i = 0;
            while(i < disciplinaDelete.size()){
                comandoExcluirAlunoDisciplina.setInt(1, matricula);
                retorno += comandoExcluirAlunoDisciplina.executeUpdate();
                i++;
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Erro ao excluir Aluno na Disciplina" + ex);
        }
        return retorno;
    }

    public AlunoVO BuscaPorMatricula(int matricula) throws PersistenciaException {
        AlunoVO alunoVO = null;

        try {
            comandoBuscarMatricula.setInt(1, matricula);
            ResultSet rs = comandoBuscarMatricula.executeQuery();
            if(rs.next()) {
                alunoVO = this.montaAluno(rs);   
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar por matricula" + e.getMessage());
        }
        return alunoVO;
    }

    public List<AlunoVO> buscarPorNome(String nome) throws PersistenciaException {
        List<AlunoVO> listaAlunos = new ArrayList();

        try {
            comandoBuscarNome.setString(1, "%" + nome.trim().toUpperCase() + "%");
            ResultSet rs = comandoBuscarNome.executeQuery();

            while (rs.next()) {              
                listaAlunos.add(montaAluno(rs));
            }
        } catch (SQLException e) {
            throw new PersistenciaException("Erro ao buscar por nome " + e.getMessage());
        }
        return listaAlunos;
    }
    
    private AlunoVO montaAluno( ResultSet rs ) throws PersistenciaException {
        AlunoVO alu = new AlunoVO( ) ;
        try{
            alu.setMatricula(rs.getInt("matricula"));
            alu.setNome(rs.getString("Nome").trim());
            alu.setNomeMae(rs.getString("nomemae").trim());
            alu.setNomePai(rs.getString("nomepai").trim());
            alu.setSexo(EnumSexo.values()[rs.getInt("sexo")]);
            alu.getEndereco().setLogradouro(rs.getString("logradouro").trim());
            alu.getEndereco().setNumero(rs.getInt("numero"));
            alu.getEndereco().setBairro(rs.getString("bairro").trim());
            alu.getEndereco().setCidade(rs.getString("cidade").trim());
            alu.getEndereco().setUf(EnumUF.valueOf(rs.getString("uf")));
            alu.setCurso(obterCurso(rs.getInt("curso")));
            alu.setDisciplinas(obterDisciplinasAluno(rs.getInt("matricula")));
        }catch(Exception ex){
            throw new PersistenciaException("Erro ao acessaros dados do resultado");
        }
        return alu;
    }
    
    public List<AlunoVO> recuperar() throws PersistenciaException{
        List<AlunoVO> listaCurso = new ArrayList();
        try{
            ResultSet resultado = comandoRecuperar.executeQuery(); //Executa a consulta SQL neste objeto PreparedStatement e retorna o objeto ResultSet gerado pela consulta
            while(resultado.next()){//ele aponta para a prmeira linha
                listaCurso.add(montaAluno(resultado));
            }
        }catch(SQLException e){
            System.out.println("Erro ao recuperar os grupos" + e.toString());
        }
        return listaCurso;
    }
    
    //Devolve o curso em para ser selecionado
    public Map<String,Integer> obterCursos() throws PersistenciaException{
        try {
            DisciplinaDAO grupoDAO = new DisciplinaDAO(conexao);
            return grupoDAO.obterCursos();
        } catch (PersistenciaException ex) {
            throw new PersistenciaException("Erro ao acessar os dados resultado: " + ex);
        }
    }
    
    //Busca o CursoVO
    public CursoVO obterCurso(int codigo){
        CursoVO cursoVO = null;
        try {
            CursoDAO grupoDAO = new CursoDAO(conexao);
            cursoVO = grupoDAO.buscaCodigo(codigo);
        } catch (PersistenciaException ex) {
            System.out.println("Erro ao obterCurso: " + ex.toString());
        }
        return cursoVO;
    }
    
    //recupera por curso
    public List<AlunoVO> recuperarPorCurso(int codigo) throws SQLException, PersistenciaException{
        List<AlunoVO> alunoVO = new ArrayList();
        comandoRecuperarPorCurso.setInt(1, codigo);
        ResultSet rs = comandoRecuperarPorCurso.executeQuery();

        while(rs.next()) {
             alunoVO.add(montaAluno(rs));   
        }
        return alunoVO;
    }
    
    //recupera as diciplinas
    public Map<String,DisciplinaVO> obterDisciplinas() throws PersistenciaException{
        DisciplinaDAO grupoDAO = null;
        Map<String,DisciplinaVO> mapDisciplina = new HashMap(); 
        try {
            grupoDAO = new DisciplinaDAO(conexao);
            List<DisciplinaVO> disciplinaVO = grupoDAO.recuperar();
            if(!disciplinaVO.isEmpty()){
                int i = 0;
                while(i < disciplinaVO.size()){
                    mapDisciplina.put(disciplinaVO.get(i).getNome(), disciplinaVO.get(i));
                    i++;
                }     
            }
        } catch (PersistenciaException ex) {
            throw new PersistenciaException("Erro em obter disciplinas: " + ex);
        }
       return mapDisciplina;
    }
    
    public List<DisciplinaVO> obterDisciplinasAluno(int matricula) throws PersistenciaException{
        List<DisciplinaVO> disciplinaVO = new ArrayList();
        try {
            ResultSet resultado = comandoRecuperarRegistro.executeQuery();
            DisciplinaDAO grupoDAO = new DisciplinaDAO(conexao);
            while(resultado.next()){
                if(resultado.getInt("aluno") == matricula){
                    disciplinaVO.add(grupoDAO.buscaCodigo(resultado.getInt("disciplina")));
                }
            }
        } catch (SQLException ex) {
            throw new PersistenciaException("Erro ao acessar os dados resultado: " + ex);
        } catch (PersistenciaException ex1) {
            throw new PersistenciaException("Erro em obter disciplinas: " + ex1);
        }     
        return disciplinaVO;
    }
}

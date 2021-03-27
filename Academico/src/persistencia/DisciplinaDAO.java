package persistencia;

import vo.CursoVO;
import vo.DisciplinaVO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Classe especializada nas operações de persistência de dados. (È inserido os dados no banco de dados)
 * Aqui tem os metodos inclusão, alteração, exclusão, busca por codigo e busca por nome
 * @author Eduardo Gonçalves da Silva
 */
public class DisciplinaDAO{
    private PreparedStatement comandoIncluir;
    private PreparedStatement comandoAlterar;
    private PreparedStatement comandoExcluir;
    private PreparedStatement comandoBuscaNome;
    private PreparedStatement comandoBuscaCodigo;
    private PreparedStatement comandoRecuperar;
    private PreparedStatement comandoRecuperarPorCurso;
    private ConexaoBD conexao;

    public DisciplinaDAO(ConexaoBD conexao) throws PersistenciaException {
        try{
            this.conexao= conexao; 
            this.comandoIncluir = this.conexao.getConexao().prepareStatement("INSERT INTO disciplina(nome,semestre,cargahoraria,curso) VALUES(?,?,?,?)");
            this.comandoAlterar = this.conexao.getConexao().prepareStatement("UPDATE disciplina SET nome=?, semestre=?, cargahoraria=?, curso=? WHERE codigo=?");
            this.comandoExcluir = this.conexao.getConexao().prepareStatement("DELETE FROM disciplina WHERE codigo=?");
            this.comandoBuscaNome = this.conexao.getConexao().prepareStatement("SELECT * FROM disciplina WHERE upper(nome) LIKE ?");
            this.comandoBuscaCodigo = this.conexao.getConexao().prepareStatement("SELECT * FROM disciplina WHERE codigo = ?");
            this.comandoRecuperar = this.conexao.getConexao().prepareStatement("SELECT * FROM disciplina ORDER BY nome");
            this.comandoRecuperarPorCurso = conexao.getConexao().prepareStatement("SELECT * FROM disciplina WHERE curso=?");
        }catch(SQLException e){
            throw new PersistenciaException("Erro ao preparar: " + e);
        }
    }
    
    public int incluir(DisciplinaVO disciplina) throws PersistenciaException{
        int retorno = 0;
        try{
            comandoIncluir.setString(1, disciplina.getNome());
            comandoIncluir.setInt(2, disciplina.getSemestre());
            comandoIncluir.setInt(3, disciplina.getCargaHoraria());
            comandoIncluir.setInt(4, disciplina.getCurso().getCodigo());
            retorno = comandoIncluir.executeUpdate();
        }catch(SQLException e){
            throw new PersistenciaException("Erro ao incluir Disciplina: " + e);
        }
        return retorno;
    }
    
    public int alterar(DisciplinaVO disciplina) throws PersistenciaException{
        int retorno;
        try{
            comandoAlterar.setString(1, disciplina.getNome());
            comandoAlterar.setInt(2, disciplina.getSemestre());
            comandoAlterar.setInt(3, disciplina.getCargaHoraria());
            comandoAlterar.setInt(4, disciplina.getCurso().getCodigo());
            comandoAlterar.setInt(5, disciplina.getCodigo());
            retorno = comandoAlterar.executeUpdate();
        }catch(SQLException e){
            throw new PersistenciaException(e + "Erro ao alterar Disciplina: "+ e);
        }
        return retorno;
    }
    
    public int excluir(int codigo) throws PersistenciaException{
        int retorno;
        try{
            comandoExcluir.setInt(1, codigo);
            retorno = comandoExcluir.executeUpdate();
        }catch(SQLException e){
            throw new PersistenciaException("Erro ao excluir Disciplina " + e);
        }
        return retorno;
    }
    
    public List<DisciplinaVO> buscaNome(String nome) throws PersistenciaException{//devolve uma lista pq pode ter varios dados com o mesmo nome
        List<DisciplinaVO> listaDisciplina = new ArrayList();
        try{
            comandoBuscaNome.setString(1, "%" + nome.toUpperCase()+"%"); //converte caractere em maiuscula
            ResultSet resultado = comandoBuscaNome.executeQuery();
            while(resultado.next()){//ele aponta para a prmeira linha
                listaDisciplina.add(montaDisciplina(resultado));
            }
        }catch(SQLException e){
            throw new PersistenciaException("(Disciplina) Erro em buscar por nome: " + e);
        }
        return listaDisciplina;
    }
    
    public DisciplinaVO buscaCodigo(int codigo) throws PersistenciaException{
        DisciplinaVO result = null;
        try {
            comandoBuscaCodigo.setInt(1, codigo);
            ResultSet resultado = comandoBuscaCodigo.executeQuery();
            if(resultado.next()){
                result = montaDisciplina(resultado);
            }
        } catch (SQLException e){
            throw new PersistenciaException("Erro em buscar por codigo em Disciplina: " + e);
        }
        return result;
    }
    
    public DisciplinaVO montaDisciplina(ResultSet resultado) throws PersistenciaException{
        DisciplinaVO disciplina = null;
        try {
            disciplina = new DisciplinaVO();
            disciplina.setCodigo(resultado.getInt("codigo"));
            disciplina.setNome(resultado.getString("nome").trim());
            disciplina.setSemestre(resultado.getInt("semestre"));
            disciplina.setCargaHoraria(resultado.getInt("cargahoraria"));
            disciplina.setCurso(obterCurso(resultado.getInt("curso")));
        } catch (SQLException e) {
           throw new PersistenciaException("Erro ao acessar os dados resultado em Disciplina: " + e);
        }
        return disciplina;
    }
    
    public List<DisciplinaVO> recuperar() throws PersistenciaException{
        List<DisciplinaVO> listaDisciplina = new ArrayList();
        try{
            ResultSet resultado = comandoRecuperar.executeQuery(); //Executa a consulta SQL neste objeto PreparedStatement e retorna o objeto ResultSet gerado pela consulta
            while(resultado.next()){//ele aponta para a prmeira linha
                listaDisciplina.add(montaDisciplina(resultado));
            }
        }catch(SQLException e){
            throw new PersistenciaException("Erro em recuperar disciplina: " + e);
        }
        return listaDisciplina;
    }
    
    //Devolve o curso em para ser selecionado
    public Map<String,Integer> obterCursos(){
        Map<String, Integer> mapCurso = new HashMap();
        
        try {
            CursoDAO grupoDAO = new CursoDAO(conexao);
            List<CursoVO> listaCurso = grupoDAO.recuperar();
            int i = 0;
            while(i< listaCurso.size() ){
                mapCurso.put(listaCurso.get(i).getNome(), listaCurso.get(i).getCodigo());
                i++;
            }
        } catch (PersistenciaException ex) {
            System.out.println("Erro ao obterCurso: " + ex.toString());
        }        
        return mapCurso;
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
    
    public List<DisciplinaVO> recuperarPorCurso(int codigo) throws SQLException, PersistenciaException{
        List<DisciplinaVO> alunoVO = new ArrayList();
        comandoRecuperarPorCurso.setInt(1, codigo);
        ResultSet rs = comandoRecuperarPorCurso.executeQuery();

        while(rs.next()) {
             alunoVO.add(montaDisciplina(rs));   
        }
        return alunoVO;
    }
}

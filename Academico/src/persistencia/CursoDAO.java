package persistencia;

import vo.CursoVO;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Classe especializada nas operações de persistência de dados. (È inserido os dados no banco de dados)
 * Aqui tem os metodos inclusão, alteração, exclusão, busca por codigo e busca por nome
 * 
 * @author Eduardo Gonçalves da Silva
 */
public class CursoDAO{
    private PreparedStatement comandoIncluir;
    private PreparedStatement comandoAlterar;
    private PreparedStatement comandoExcluir;
    private PreparedStatement comandoBuscaNome;
    private PreparedStatement comandoBuscaCodigo;
    private PreparedStatement comandoRecuperar;
    private ConexaoBD conexao;

    public CursoDAO(ConexaoBD conexao) throws PersistenciaException {
        this.conexao = conexao;
        try{
            comandoIncluir = conexao.getConexao().prepareStatement("INSERT INTO curso(nome,descricao) VALUES(?,?)");
            comandoAlterar = conexao.getConexao().prepareStatement("UPDATE curso SET nome=?, descricao=? WHERE codigo=?");
            comandoExcluir = conexao.getConexao().prepareStatement("DELETE FROM curso WHERE codigo=?");
            comandoBuscaNome = conexao.getConexao().prepareStatement("SELECT * FROM curso WHERE upper(nome) LIKE ?");
            comandoBuscaCodigo = conexao.getConexao().prepareStatement("SELECT * FROM curso WHERE codigo = ?");
            comandoRecuperar = conexao.getConexao().prepareStatement("SELECT * FROM curso ORDER BY nome");
        }catch(SQLException e){
            throw new PersistenciaException("Erro ao preparar: " + e);
        }
    }
    
    public int incluir(CursoVO curso) throws PersistenciaException{
        int retorno = 0;
        try{
            comandoIncluir.setString(1, curso.getNome());
            comandoIncluir.setString(2, curso.getDescricao());
            retorno = comandoIncluir.executeUpdate();
        }catch(SQLException e){
            throw new PersistenciaException("Erro ao incluir: " + e);
        }
        return retorno;
    }
    
    public int alterar(CursoVO curso) throws PersistenciaException{
        int retorno;
        try{
            comandoAlterar.setString(1, curso.getNome());
            comandoAlterar.setString(2, curso.getDescricao());
            comandoAlterar.setInt(3, curso.getCodigo());
            retorno = comandoAlterar.executeUpdate();
        }catch(SQLException e){
            throw new PersistenciaException(e + "Erro ao alterar: "+ e);
        }
        return retorno;
    }
    
    public int excluir(int codigo) throws PersistenciaException{
        int retorno;
        try{
            comandoExcluir.setInt(1, codigo);
            retorno = comandoExcluir.executeUpdate();
        }catch(SQLException e){
            throw new PersistenciaException("Erro ao excluir " + e);
        }
        return retorno;
    }
    
    public List<CursoVO> buscaNome(String nome) throws PersistenciaException{//devolve uma lista pq pode ter varios dados com o mesmo nome
        List<CursoVO> listaCurso = new ArrayList();
        try{
            comandoBuscaNome.setString(1, "%" + nome.toUpperCase()+"%");
            ResultSet resultado = comandoBuscaNome.executeQuery();
            while(resultado.next()){//ele aponta para a prmeira linha
                listaCurso.add(montaCurso(resultado));
            }
        }catch(SQLException e){
            throw new PersistenciaException("Erro em buscar por nome: " + e);
        }
        return listaCurso;
    }
    
    public CursoVO buscaCodigo(int codigo) throws PersistenciaException{
        CursoVO result = null;
        try {
            comandoBuscaCodigo.setInt(1, codigo);
            ResultSet resultado = comandoBuscaCodigo.executeQuery();
            if(resultado.next()){
                result = montaCurso(resultado);
            }
        } catch (SQLException e){
            throw new PersistenciaException("Erro em buscar por codigo: " + e);
        }
        return result;
    }
    
    public CursoVO montaCurso(ResultSet resultado) throws PersistenciaException{
        CursoVO curso = null;
        try {
            curso = new CursoVO();
            curso.setCodigo(resultado.getInt("codigo"));
            curso.setNome(resultado.getString("nome").trim());
            curso.setDescricao(resultado.getString("descricao").trim());
            
        } catch (SQLException e) {
           throw new PersistenciaException("Erro ao acessar os dados resultado: " + e);
        }
        return curso;
    }
    
    public List<CursoVO> recuperar() throws PersistenciaException{
        List<CursoVO> listaCurso = new ArrayList();
        try{
            ResultSet resultado = comandoRecuperar.executeQuery(); //Executa a consulta SQL neste objeto PreparedStatement e retorna o objeto ResultSet gerado pela consulta
            while(resultado.next()){//ele aponta para a prmeira linha
                listaCurso.add(montaCurso(resultado));
            }
        }catch(SQLException e){
            System.out.println("Erro ao recuperar os grupos" + e.toString());
        }
        return listaCurso;
    }
}

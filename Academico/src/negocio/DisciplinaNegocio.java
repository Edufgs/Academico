package negocio;

import java.sql.SQLException;
import vo.CursoVO;
import persistencia.DisciplinaDAO;
import persistencia.ConexaoBD;
import persistencia.PersistenciaException;
import vo.DisciplinaVO;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 * Faz a validação dos dados
 * @author Eduardo Gonçalves da Silva
 */

public class DisciplinaNegocio {
    private DisciplinaDAO disciplinaDAO;

    public DisciplinaNegocio() throws NegocioException, PersistenciaException {
        try {
            this.disciplinaDAO = new DisciplinaDAO(ConexaoBD.getInstacia());
        } catch (PersistenciaException ex) {
            throw new NegocioException("Erro ao iniciar a DAO: " + ex);
        }
    }
    
    public void inserir(DisciplinaVO disciplinaVO) throws NegocioException{
        String mensagemErros = this.validaDados(disciplinaVO);
        
        if(!mensagemErros.isEmpty()){
            throw new NegocioException(mensagemErros+", em disciplina");
        }
        try{
            if(disciplinaDAO.incluir(disciplinaVO) == 0){
               throw new NegocioException("Inclusão não realizada em disciplina"); 
            }
        } catch (PersistenciaException ex) {
            throw new NegocioException("Erro ao incluir a disciplina: " + ex);
        }
    }
    
    public void alterar(DisciplinaVO disciplinaVO) throws NegocioException{
        String mensagemErros = this.validaDados(disciplinaVO);
        
        if(disciplinaVO.getCodigo() <= 0){
            mensagemErros+="O codigo não pode ser vazio!!!";
        }
        
        if(mensagemErros.isEmpty()!= true){
            throw new NegocioException(mensagemErros);
        }
        try{
            if(disciplinaDAO.alterar(disciplinaVO) == 0){
               throw new NegocioException("Inclusão não realizada"); 
            }
        } catch (PersistenciaException ex) {
            throw new NegocioException("Erro ao incluir: " + ex);
        }
    }
    
    public void excluir(int codigo) throws NegocioException{
        try{
            if(disciplinaDAO.excluir(codigo) == 0){
               throw new NegocioException("Exclusão não realizada");  
            }else{
                JOptionPane.showMessageDialog(null, "Excluido com sucesso!");
            }
        } catch (PersistenciaException ex) {
            throw new NegocioException("Erro ao excluir: " + ex);
        }
    }
    
    public List<DisciplinaVO> pesquisaNome(String nome) throws NegocioException{
        try{
            return disciplinaDAO.buscaNome(nome);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Erro ao pesquisar com o nome: " + ex);
        }
    }
    
    public DisciplinaVO pesquisaCodigo(int codigo) throws NegocioException{
        try{
            return disciplinaDAO.buscaCodigo(codigo);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Erro ao pesquisar com codigo: " + ex);
        }
    }
    
    public String validaDados(DisciplinaVO disciplinaVO){
        String mensagemErros = "";
        
        if(disciplinaVO.getNome() == null){
            mensagemErros +="\n Nome não pode ser vazio";
        }
        if(disciplinaVO.getSemestre() <=0){
          mensagemErros +="\n Semestre não pode ser vazio";  
        }
        if(disciplinaVO.getCargaHoraria() <= 0){
            mensagemErros +="\n Carga Horaria não pode ser vazio";
        }
        if(disciplinaVO.getCurso() == null){
            mensagemErros +="\n Curso não pode ser vazio";
        }
        return mensagemErros;
    }
    
    public List<DisciplinaVO> recuperar() throws NegocioException{
        try{
            return disciplinaDAO.recuperar();
        } catch (PersistenciaException ex) {
            throw new NegocioException("Erro ao pesquisar com codigo: " + ex);
        } 
    }
    
    public Map<String,Integer> obterCursos(){
        return disciplinaDAO.obterCursos();
    }
    
    public CursoVO obterCurso(int codigo){
        return disciplinaDAO.obterCurso(codigo);
    }
    
    public List<DisciplinaVO> recuperarPorCurso(int codigo) throws PersistenciaException, SQLException{
         return disciplinaDAO.recuperarPorCurso(codigo);
     }
}

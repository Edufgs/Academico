package negocio;

import persistencia.ConexaoBD;
import persistencia.CursoDAO;
import persistencia.PersistenciaException;
import vo.CursoVO;
import java.util.List;
import javax.swing.JOptionPane;

/**
 * Faz a validação dos dados
 * 
 * @author Eduardo Gonçalves da Silva
 */
public class CursoNegocio {
    private CursoDAO cursoDAO;

    public CursoNegocio() throws NegocioException {
        try {
            this.cursoDAO = new CursoDAO(ConexaoBD.getInstacia());
        } catch (PersistenciaException ex) {
            throw new NegocioException("Erro ao iniciar a Persistencia: " + ex);
        }
    }
    
    public void inserir(CursoVO cursoVO) throws NegocioException{
        String mensagemErros = this.validaDados(cursoVO);
        
        if(!mensagemErros.isEmpty()){
            throw new NegocioException(mensagemErros);
        }
        try{
            if(cursoDAO.incluir(cursoVO) == 0){
               throw new NegocioException("Inclusão não realizada"); 
            }
        } catch (PersistenciaException ex) {
            throw new NegocioException("Erro ao incluir: " + ex);
        }
    }
    
    public void alterar(CursoVO cursoVO) throws NegocioException{
        String mensagemErros = this.validaDados(cursoVO);
        
        if(cursoVO.getCodigo() <= 0){
            mensagemErros+="O codigo não pode ser vazio!!!";
        }
        
        if(mensagemErros.isEmpty()!= true){
            throw new NegocioException(mensagemErros);
        }
        try{
            if(cursoDAO.alterar(cursoVO) == 0){
               throw new NegocioException("Inclusão não realizada"); 
            }
        } catch (PersistenciaException ex) {
            throw new NegocioException("Erro ao incluir: " + ex);
        }
    }
    
    public void excluir(int codigo) throws NegocioException{
        try{
            if(cursoDAO.excluir(codigo) == 0){
               throw new NegocioException("Exclusão não realizada");  
            }else{
                JOptionPane.showMessageDialog(null,"Exclusão feita com sucesso!!!");
            }
        } catch (PersistenciaException ex) {
            throw new NegocioException("Erro ao excluir: " + ex);
        }
    }
    
    public List<CursoVO> pesquisaNome(String nome) throws NegocioException{
        try{
            return cursoDAO.buscaNome(nome);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Erro ao pesquisar com o nome: " + ex);
        }
    }
    
    public CursoVO pesquisaCodigo(int codigo) throws NegocioException{
        try{
            return cursoDAO.buscaCodigo(codigo);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Erro ao pesquisar com codigo: " + ex);
        }
    }
    
    public String validaDados(CursoVO cursoVO){
        String mensagemErros = "";
        
        if(cursoVO.getNome() == null){
            mensagemErros +="\n Nome não pode ser vazio";
        }
        
        return mensagemErros;
    }
    
    public List<CursoVO> recuperar() throws PersistenciaException{
        return cursoDAO.recuperar();
    }
}

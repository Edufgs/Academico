package negocio;

import java.sql.SQLException;
import persistencia.AlunoDAO;
import java.util.List;
import java.util.Map;
import persistencia.ConexaoBD;
import persistencia.PersistenciaException;
import vo.AlunoVO;
import vo.CursoVO;
import vo.DisciplinaVO;

public class AlunoNegocio {

    private AlunoDAO alunoDAO;

    public AlunoNegocio() throws NegocioException {
        try {
            this.alunoDAO = new AlunoDAO(ConexaoBD.getInstacia());
        } catch (PersistenciaException e) {
            throw new NegocioException("Erro ao iniciar percistencia - " + e.getMessage());
        }
    }

    public void inserir(AlunoVO alunoVO) throws NegocioException, PersistenciaException {
        String mensagemErro = validarDados(alunoVO);

        if (!mensagemErro.isEmpty()) {
            throw new NegocioException(mensagemErro);
        }

        if (alunoDAO.incluir(alunoVO) == 0) {
            throw new NegocioException("inclusão não realizada");
        }
    }

    public void alterar(AlunoVO alunoVO) throws NegocioException, PersistenciaException {
        String mensagemErro = validarDados(alunoVO);
        if (!mensagemErro.isEmpty()) {
            throw new NegocioException(mensagemErro);
        }

        if (alunoDAO.alterar(alunoVO) == 0) {
            throw new NegocioException("alteração não realizada");
        }
    }

    public void excluir(int codigo) throws NegocioException, PersistenciaException {

        if (alunoDAO.excluir(codigo) == 0) {
            throw new NegocioException("exclusão não realizada");
        }
    }

    public List<AlunoVO> pesquisaParteNome(String nome) throws PersistenciaException, NegocioException{

        try {
            return alunoDAO.buscarPorNome(nome);
        } catch (PersistenciaException ex) {
            throw new NegocioException("Erro ao pesquisar aluno pelo nome: "+ex.getMessage());
        }
    }

    public AlunoVO pesquisarMatricula(int codigo) throws PersistenciaException{

        return alunoDAO.BuscaPorMatricula(codigo);
    }

    private String validarDados(AlunoVO alunoVO) {
        String mensagemErro = "";

        if (alunoVO.getNome() == null || alunoVO.getNome().length() == 0) {
            mensagemErro += "\nNome do aluno não pode ser vazio";
        }

        if (alunoVO.getNomeMae() == null || alunoVO.getNomeMae().length() == 0) {
            mensagemErro += "\nNome da mãe do aluno não pode ser vazio";
        }

        if (alunoVO.getNomePai() == null || alunoVO.getNomePai().length() == 0) {
            mensagemErro += "\nNome do pai do aluno não pode ser vazio";
        }

        if (alunoVO.getSexo() == null) {
            mensagemErro += "\nSexo do aluno não pode ser nulo";
        }

        if (alunoVO.getEndereco().getLogradouro() == null || alunoVO.getEndereco().getLogradouro().length() == 0) {
            mensagemErro += "\nLogradouro não pode ser vazio";
        }

        if (alunoVO.getEndereco().getNumero() <= 0) {
            mensagemErro += "\nNumero não pode ser menor ou igual a zero";
        }

        if (alunoVO.getEndereco().getBairro() == null || alunoVO.getEndereco().getBairro().length() == 0) {
            mensagemErro += "\nO bairro não pode ser vazio";
        }

        if (alunoVO.getEndereco().getCidade() == null || alunoVO.getEndereco().getCidade().length() == 0) {
            mensagemErro += "\nA cidade não pode ser vazia";
        }

        if (alunoVO.getEndereco().getUf() == null) {
            mensagemErro += "\nUF não pode ser vazia";
        }
        if (alunoVO.getCurso().getCodigo() <= 0) {
            mensagemErro += "\nCurso não pode ser vazio";
        }
        return mensagemErro;
    }
    
    public List<AlunoVO> recuperar() throws PersistenciaException{
        return alunoDAO.recuperar();
    }
    
    public Map<String,Integer> obterCursos() throws PersistenciaException{
        return alunoDAO.obterCursos();
    }
    
    public CursoVO obterCurso(int codigo){
        return alunoDAO.obterCurso(codigo);
    }
    
     public List<AlunoVO> recuperarPorCurso(int codigo) throws SQLException, PersistenciaException{
         return alunoDAO.recuperarPorCurso(codigo);
     }
     
     public Map<String,DisciplinaVO> obterDisciplinas() throws PersistenciaException{
         return alunoDAO.obterDisciplinas();
     }
     
     public void cadastraDisciplina(AlunoVO alunoVO) throws PersistenciaException, NegocioException{
         if(alunoDAO.cadastrarAlunoDisciplina(alunoVO) == 0) {
            throw new NegocioException("inclusão não realizada");
        }
     }
}

package execucao;

import static execucao.Principal.menuPrincipal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;
import negocio.AlunoNegocio;
import negocio.NegocioException;
import persistencia.ConexaoBD;
import persistencia.PersistenciaException;
import vo.AlunoVO;
import vo.DisciplinaVO;
import vo.EnumSexo;
import vo.EnumUF;

/**
 * Classe menu responsavel pela parte Alunos.
 * 
 * @author Eduardo Gonçalves da Silva
 */
public class MenuAluno {
    private static AlunoNegocio alunoNegocio;
    
    /**
    * Metodo responsavel pela tela menu aluno.
    * 
    */
    
    public static void menuAluno() throws PersistenciaException{
        try{
            alunoNegocio =new AlunoNegocio();
        }catch(NegocioException ex){
            System.out.println("Camada de negocio e persistencia não iniciada: " + ex);
        }
        if(alunoNegocio != null){
            EnumMenuAluno opcao = EnumMenuAluno.SAIR;
            do{
                try{
                    opcao = exibirMenu();
                    switch(opcao){
                        case INCLUIR:
                            incluir();
                            break;
                        case ALTERAR:
                            alterar();
                            break;
                        case EXCLUIR:
                            excluir();
                            break;
                        case PESQUISARNOME:
                            pesquisarNome();
                            break;
                        case PESQUISARCODIGO:
                            pesquisarCodigo();
                            break;
                        case RECUPERAR:
                            recuperar();
                            break;
                        case LISTAR:
                            listar();
                            break;
                        case CADASTRARALUNODISCIPLINAS:
                            cadastraDisciplina();
                            break;     
                        case VOLTAR:
                            ConexaoBD.getInstacia().desconectar();
                            menuPrincipal();
                            break;
                    }
                } catch (NegocioException ex) {
                    System.out.println("Operação não realizada corretamente "+ex);
                }
            }while(opcao != EnumMenuAluno.SAIR);
        }
        ConexaoBD.getInstacia().desconectar();
        System.exit(0);
    }
    
    /**
    * Metodo que inclui alunos no banco de dados.
    * 
    */
    public static void incluir() throws PersistenciaException, NegocioException{
        alunoNegocio.inserir(lerDados());
    }
    
    /**
    * Metodo que altera dados dos alunos no banco de dados.
    * 
    */
    public static void alterar() throws NegocioException, PersistenciaException{
        AlunoVO alunoVO = null;
        try{
            alunoVO = alunoNegocio.pesquisarMatricula(Integer.parseInt(JOptionPane.showInputDialog("Digite o codigo")));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Digitação inconsistente: " + e);
        }
        if(alunoVO != null){
            AlunoVO alunoTemp = lerDados(alunoVO);
            alunoTemp.setMatricula(alunoVO.getMatricula()); 
            alunoNegocio.alterar(alunoTemp);
            JOptionPane.showMessageDialog(null, "Alterado com sucesso");
        }else{
            JOptionPane.showMessageDialog(null, "Nada foi encontrado");
        }
    }
    
    /**
    * Metodo que exclui alunos no banco de dados.
    * 
    */
    public static void excluir() throws NegocioException, PersistenciaException{
        AlunoVO AlunoVO = null;
        try{
            AlunoVO = alunoNegocio.pesquisarMatricula(Integer.parseInt(JOptionPane.showInputDialog("Digite o codigo")));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Digitação inconsistente: " + e);
        }
        if(AlunoVO != null){
            alunoNegocio.excluir(AlunoVO.getMatricula());
        }else{
            JOptionPane.showMessageDialog(null, "Nada foi encontrado");
        }
    }
    
    /**
    * Metodo que pesquisa por nome alunos no banco de dados.
    * 
    */
    public static void pesquisarNome() throws NegocioException, PersistenciaException{
        String nome = JOptionPane.showInputDialog("Digite o nome");
        if(nome != null){
            List<AlunoVO> listaVO = alunoNegocio.pesquisaParteNome(nome);
            if(listaVO.isEmpty() != true){
                System.out.println(Arrays.toString(listaVO.toArray()));
            }else{
                JOptionPane.showMessageDialog(null, "Nada foi encontrado");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Nome não pode ser Nulo");
        }
    }
    
    /**
    * Metodo que pesquisa por codigo alunos no banco de dados.
    * 
    */
    public static void pesquisarCodigo(){
        AlunoVO alunoVO = null;
        try{
            alunoVO = alunoNegocio.pesquisarMatricula(Integer.parseInt(JOptionPane.showInputDialog("Digite o codigo")));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Digitação inconsistente: " + e);
        }
        if(alunoVO != null){
            System.out.println(alunoVO);
        }else{
            JOptionPane.showMessageDialog(null, "Nada foi encontrado");
        }
    }
    
    /**
    * Metodo para digitar os dados e adiciona em um objeto.
    * 
    * @param alunoTemp Objeto usado para adicionar ou editar dados.
    * @return Retorna um objeto para ser inserido no banco
    */
    public static AlunoVO lerDados(AlunoVO alunoTemp) throws PersistenciaException{
        Map<String,Integer> listaCursos = alunoNegocio.obterCursos();
        alunoTemp.setNome(JOptionPane.showInputDialog("Digite o nome", alunoTemp.getNome()));
        alunoTemp.setNomePai(JOptionPane.showInputDialog("Digite o nome do Pai",alunoTemp.getNomePai()));
        alunoTemp.setNomeMae(JOptionPane.showInputDialog("Digite o nome da Mãe", alunoTemp.getNomeMae()));
        alunoTemp.setSexo((EnumSexo) JOptionPane.showInputDialog(null, "Escolha uma Opção", "Dados",JOptionPane.QUESTION_MESSAGE,null,EnumSexo.values(),alunoTemp.getSexo()));
        alunoTemp.getEndereco().setLogradouro(JOptionPane.showInputDialog("Digite o Logradouro", alunoTemp.getEndereco().getLogradouro()));
        alunoTemp.getEndereco().setNumero(Integer.parseInt(JOptionPane.showInputDialog("Digite o numero", alunoTemp.getEndereco().getNumero())));
        alunoTemp.getEndereco().setBairro(JOptionPane.showInputDialog("Digite o Bairro", alunoTemp.getEndereco().getBairro()));
        alunoTemp.getEndereco().setCidade(JOptionPane.showInputDialog("Digite a Cidade", alunoTemp.getEndereco().getCidade()));
        alunoTemp.getEndereco().setUf((EnumUF) JOptionPane.showInputDialog(null, "Escolha uma Opção", "Dados",JOptionPane.QUESTION_MESSAGE,null,EnumUF.values(),alunoTemp.getEndereco().getUf())); 
        alunoTemp.setCurso(alunoNegocio.obterCurso(listaCursos.get((String)JOptionPane.showInputDialog(null,"Escolha o curso","Cursos", JOptionPane.QUESTION_MESSAGE, null, listaCursos.keySet().toArray(),alunoTemp.getCurso().getNome()))));        
        return alunoTemp;
    }
    
    /**
    * Metodo para Cadastrar Aluno nas Disciplinas
    * 
    */
    public static void cadastraDisciplina() throws PersistenciaException, NegocioException{
        AlunoVO alunoTemp = alunoNegocio.pesquisarMatricula(Integer.parseInt(JOptionPane.showInputDialog("Digite o codigo")));
        if(alunoTemp == null){
            JOptionPane.showMessageDialog(null, "Nada foi encontrado");
            menuAluno();
        }
        List<DisciplinaVO> listaDisciplina = new ArrayList(); //o retorno
        Map<String,DisciplinaVO> listaOpcao = alunoNegocio.obterDisciplinas();
        List<DisciplinaVO> lista = alunoTemp.getDisciplinas();
        do{
            DisciplinaVO disciplinaVO = listaOpcao.get((String)JOptionPane.showInputDialog(null,"Escolha a disciplina","Disciplinas", JOptionPane.QUESTION_MESSAGE, null, listaOpcao.keySet().toArray(), listaOpcao.keySet().toArray()[0]));
            int i = 0;
            boolean verif = false; 
            while(i < lista.size()){
                if(lista.get(i).getCodigo() == disciplinaVO.getCodigo()){
                    verif = true;
                    JOptionPane.showMessageDialog(null, "Aluno ja está cadastrado nessa disciplina");
                    break;
                }
                i++;
            }
            if(verif == false){
                listaDisciplina.add(disciplinaVO);
            }
        }while(JOptionPane.showConfirmDialog(null, "Deseja cadastrar mais diciplinas?", "Escolha a opção",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION);
        alunoTemp.setDisciplinas(listaDisciplina);
        alunoNegocio.cadastraDisciplina(alunoTemp);
    }
    
    /**
    * Metodo que cria um objeto para ser usado em "lerDados(AlunoVO alunoTemp)".
    * 
    * @return Retorna um objeto para ser inserido no banco
    */
    public static AlunoVO lerDados() throws PersistenciaException{
        AlunoVO alunoTemp = new AlunoVO();
        return lerDados(alunoTemp);
    } 
    
    /**
    * Metodo para listar os dados do banco de dados 
    * 
    */
    public static void listar() throws NegocioException, PersistenciaException{
            EnumListarAluno opcao = EnumListarAluno.SAIR;
            do{
                List<AlunoVO> listaAluno = null;
                int i = 0;
                try{
                    opcao = exibirMenuListar();
                    switch(opcao){
                        case TUDO:
                            listaAluno = alunoNegocio.recuperar();
                            i=0;
                            while(i < listaAluno.size()){
                                System.out.print(listaAluno.get(i).getListar());
                                i++;
                            }                    
                            break;
                        case PORCURSO:
                            Map<String,Integer> listaCursos = alunoNegocio.obterCursos();
                            int curso = listaCursos.get((String)JOptionPane.showInputDialog(null,"Escolha o curso","Cursos", JOptionPane.QUESTION_MESSAGE, null, listaCursos.keySet().toArray(),listaCursos.keySet().toArray()[0]));
                            System.out.println(alunoNegocio.obterCurso(curso).getListar());
                            listaAluno = alunoNegocio.recuperarPorCurso(curso);
                            if(listaAluno == null){
                                System.out.println("Esse curso não tem alunos!!!");
                                break;
                            }
                            i=0;
                            while(i < listaAluno.size()){
                                System.out.println("Matricula= " + listaAluno.get(i).getMatricula()+ " Nome= " + listaAluno.get(i).getNome());
                                i++;
                            }
                            break;
                        case DISCIPLINAALUNO:
                            List<AlunoVO> alunoVO = alunoNegocio.recuperar();                                                        
                            while(i < alunoVO.size()){
                                int j = 0;
                                System.out.println("Matricula: " + alunoVO.get(i).getMatricula() + " Nome: "+ alunoVO.get(i).getNome());
                                while(j < alunoVO.get(i).getDisciplinas().size()){
                                    System.out.println("Codigo: " + alunoVO.get(i).getDisciplinas().get(j).getCodigo() + " Nome: " + alunoVO.get(i).getDisciplinas().get(j).getNome());
                                    j++;
                                }
                                i++;
                            }
                            break;
                        case VOLTAR:
                            menuAluno();
                            break;
                    }
                } catch (SQLException ex) {
                    System.out.println("Operação não realizada corretamente "+ex);
                } catch (PersistenciaException ex) {
                    throw new NegocioException("Erro na listagem: "+ex.getMessage());
            }
            }while(opcao != EnumListarAluno.SAIR);
            ConexaoBD.getInstacia().desconectar();
            System.exit(0);
    }
    
    /**
    * Metodo para escoher a opção no menu da lista
    * 
    * @return retorna a opção escolhida
    */    
    private static EnumListarAluno exibirMenuListar(){
        EnumListarAluno opcao = (EnumListarAluno)JOptionPane.showInputDialog(null, "Escolha um Opção", "Menu", JOptionPane.QUESTION_MESSAGE, null, EnumListarAluno.values(), EnumListarAluno.values()[0]);
        return opcao;
    }

    /**
    * Metodo para escoher a opção no menu
    * 
    * @return retorna a opção escolhida
    */    
    private static EnumMenuAluno exibirMenu(){
        EnumMenuAluno opcao;
        /*Explicação:
            (EnumMenu)showInternalInputDialog(Component parentComponent, Object message, String title, int messageType, Icon icon, Object[] selectionValues, Object initialSelectionValue);
            (EnumMenu) = Transforma em "enum constantes"
            Component parentComponent = Um componente é um objeto que possui uma representação gráfica que pode ser exibida na tela e que pode interagir com o usuário. Exemplos de componentes são os botões, caixas de seleção e barras de rolagem de uma interface gráfica de usuário típica.(o componente pai da caixa de diálogo)
            Object message = Uma mensagem para mostrar na tela (o objeto a ser exibido)
            String title = O titulo da janela (a string a ser exibida na barra de título da caixa de diálogo)
            int messageType = o tipo de mensagem a ser exibida: (ERROR_MESSAGE, INFORMATION_MESSAGE, WARNING_MESSAGE, QUESTION_MESSAGE ou PLAIN_MESSAGE)
            Icon icon = a imagem do ícone para exibir a seleção
            Object[] selectionValues = uma matriz de objetos que dá as seleções possíveis initialSelection (Opções a ser selecionadas)
            Object initialSelectionValue = o valor usado para inicializar o campo de entrada (O valor que vai ser inicializado)
        */
        opcao = (EnumMenuAluno)JOptionPane.showInputDialog(null, "Escolha uma Opção", "Menu", JOptionPane.QUESTION_MESSAGE, null, EnumMenuAluno.values(), EnumMenuAluno.values()[0]);
        return opcao;
    }
    
    /**
    * Metodo para recupera todos dados do banco de dados 
    * 
    */
    private static void recuperar() throws PersistenciaException{
        System.out.println(alunoNegocio.recuperar());
    }
}


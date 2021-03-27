package execucao;

import static execucao.Principal.menuPrincipal;
import java.sql.SQLException;
import negocio.DisciplinaNegocio;
import vo.DisciplinaVO;
import persistencia.ConexaoBD;
import negocio.NegocioException;
import persistencia.PersistenciaException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import javax.swing.JOptionPane;

/**
 *
 * @author Eduardo Gonçalves da Silva
 */
public class MenuDisciplina {
    private static DisciplinaNegocio disciplinaNegocio;
    
    public static void menuDisciplina() throws PersistenciaException{
        try{
            disciplinaNegocio = new DisciplinaNegocio();
        }catch(NegocioException ex){
            System.out.println("Camada de negocio e persistencia não iniciada: " + ex);
        }
        if(disciplinaNegocio != null){
            EnumMenu opcao = EnumMenu.SAIR;
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
                        case VOLTAR:
                            ConexaoBD.getInstacia().desconectar();
                            menuPrincipal();
                            break;
                    }
                } catch (NegocioException ex) {
                    System.out.println("Operação não realizada corretamente "+ex);
                }
            }while(opcao != EnumMenu.SAIR);
        }
        ConexaoBD.getInstacia().desconectar();
        System.exit(0);
    }
    
    public static void incluir() throws NegocioException{
        disciplinaNegocio.inserir(lerDados());
    }
    
    public static void alterar() throws NegocioException{
        DisciplinaVO disciplinaVO = null;
        try{
            disciplinaVO = disciplinaNegocio.pesquisaCodigo(Integer.parseInt(JOptionPane.showInputDialog("Digite o codigo")));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Digitação inconsistente: " + e);
        }
        if(disciplinaVO != null){
            DisciplinaVO cursoTemp = lerDados(disciplinaVO);
            cursoTemp.setCodigo(disciplinaVO.getCodigo()); 
            disciplinaNegocio.alterar(cursoTemp);
            JOptionPane.showMessageDialog(null, "Alterado com sucesso");
        }else{
            JOptionPane.showMessageDialog(null, "Nada foi encontrado");
        }
    }
    
    public static void excluir() throws NegocioException{
        DisciplinaVO disciplinaVO = null;
        try{
            disciplinaVO = disciplinaNegocio.pesquisaCodigo(Integer.parseInt(JOptionPane.showInputDialog("Digite o codigo")));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Digitação inconsistente: " + e);
        }
        if(disciplinaVO != null){
            disciplinaNegocio.excluir(disciplinaVO.getCodigo());
        }else{
            JOptionPane.showMessageDialog(null, "Nada foi encontrado");
        }
    }
    
    public static void pesquisarNome() throws NegocioException{
        String nome = JOptionPane.showInputDialog("Digite o nome");
        if(nome != null){
            List<DisciplinaVO> listaVO = disciplinaNegocio.pesquisaNome(nome);
            if(listaVO.isEmpty() != true){
                System.out.println("===============================================================================================================================================");
                System.out.println(Arrays.toString(listaVO.toArray()));
                System.out.println("===============================================================================================================================================");
            }else{
                JOptionPane.showMessageDialog(null, "Nada foi encontrado");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Nome não pode ser Nulo");
        }
    }
    
    public static void pesquisarCodigo(){
        DisciplinaVO disciplinaVO = null;
        try{
            disciplinaVO = disciplinaNegocio.pesquisaCodigo(Integer.parseInt(JOptionPane.showInputDialog("Digite o codigo")));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Digitação inconsistente: " + e);
        }
        if(disciplinaVO != null){
            System.out.println("===============================================================================================================================================");
            System.out.println(disciplinaVO);
            System.out.println("===============================================================================================================================================");
        }else{
            JOptionPane.showMessageDialog(null, "Nada foi encontrado");
        }
    }
    
    public static DisciplinaVO lerDados(DisciplinaVO disciplinaTemp){
      Map<String,Integer> listaCursos = disciplinaNegocio.obterCursos();
      disciplinaTemp.setNome(JOptionPane.showInputDialog("Digite o nome",disciplinaTemp.getNome()));
      disciplinaTemp.setSemestre(Integer.parseInt(JOptionPane.showInputDialog("Digite o semestre",disciplinaTemp.getSemestre())));
      disciplinaTemp.setCargaHoraria(Integer.parseInt(JOptionPane.showInputDialog("Digite a Carga Horaria",disciplinaTemp.getCargaHoraria())));
      disciplinaTemp.setCurso(disciplinaNegocio.obterCurso(listaCursos.get((String)JOptionPane.showInputDialog(null, "Escolha o Curso", "Curso", JOptionPane.QUESTION_MESSAGE, null, listaCursos.keySet().toArray(), disciplinaTemp.getCurso().getNome()))));
      return disciplinaTemp;
    }
    
    public static DisciplinaVO lerDados(){
        DisciplinaVO DisciplinaTemp = new DisciplinaVO();
        return lerDados(DisciplinaTemp);
    }   
    
    public static void recuperar() throws NegocioException{
        try{
            System.out.println("===============================================================================================================================================");
            System.out.println(disciplinaNegocio.recuperar());
            System.out.println("===============================================================================================================================================");
        }catch(NegocioException ex){
            System.out.println("Erro ao recuperar: " + ex);
        }
        
    }
    
    private static void listar() throws NegocioException, PersistenciaException{  
        EnumListar opcao = EnumListar.SAIR;
        do{    
            List<DisciplinaVO> listaDisciplina = null;
                try{
                    opcao = exibirMenuListar();
                    switch(opcao){
                        case TUDO:
                            listaDisciplina = disciplinaNegocio.recuperar();
                            int i =0;
                            while(i<listaDisciplina.size()){
                                System.out.println(listaDisciplina.get(i).getListar());
                                i++;
                            }                   
                            break;
                        case PORCURSO:
                            Map<String,Integer> listaCursos = disciplinaNegocio.obterCursos();
                            int curso = listaCursos.get((String)JOptionPane.showInputDialog(null,"Escolha o curso","Cursos", JOptionPane.QUESTION_MESSAGE, null, listaCursos.keySet().toArray(),listaCursos.keySet().toArray()[0]));
                            System.out.println(disciplinaNegocio.obterCurso(curso).getListar());
                            listaDisciplina = disciplinaNegocio.recuperarPorCurso(curso);
                            if(listaDisciplina == null){
                                System.out.println("Esse curso não tem alunos!!!");
                                break;
                            }
                            i=0;
                            while(i < listaDisciplina.size()){
                                System.out.println("Codigo= " + listaDisciplina.get(i).getCodigo()+ " Nome= " + listaDisciplina.get(i).getNome());
                                i++;
                            }
                            break;
                        case VOLTAR:
                            menuDisciplina();
                            break;
                    }
                } catch (SQLException ex) {
                    System.out.println("Operação não realizada corretamente "+ex);
                } catch (PersistenciaException ex) {
                    throw new NegocioException("Erro na listagem: "+ex.getMessage());
            }
        }while(opcao != EnumListar.SAIR);
            ConexaoBD.getInstacia().desconectar();
            System.exit(0);
    }
    
    private static EnumListar exibirMenuListar(){
        EnumListar opcao = (EnumListar)JOptionPane.showInputDialog(null, "Escolha um Opção", "Menu", JOptionPane.QUESTION_MESSAGE, null, EnumListar.values(), EnumListar.values()[0]);
        return opcao;
    }
    
    private static EnumMenu exibirMenu(){
        EnumMenu opcao;
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
        opcao = (EnumMenu)JOptionPane.showInputDialog(null, "Escolha uma Opção", "Menu", JOptionPane.QUESTION_MESSAGE, null, EnumMenu.values(), EnumMenu.values()[0]);
        return opcao;
    }
}

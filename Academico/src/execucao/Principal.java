package execucao;
import static execucao.MenuAluno.menuAluno;
import static execucao.MenuCurso.menuCurso;
import static execucao.MenuDisciplina.menuDisciplina;
import persistencia.PersistenciaException;
import javax.swing.JOptionPane;

/**
 *
 * @author Eduardo Gonçalves da Silva
 */
public class Principal {
     
    public static void main(String[] args) throws PersistenciaException {
        menuPrincipal();
    }
    public static void menuPrincipal() throws PersistenciaException{
            EnumMenuPrincipal opcao;
            do{
                opcao = exibirMenu();
                switch(opcao){
                    case ALUNO:
                        menuAluno();
                        break;
                    case CURSO:
                        menuCurso();
                        break;
                    case DISCIPLINA:
                        menuDisciplina();
                        break;
                    default:
                        opcao = EnumMenuPrincipal.SAIR;
                }
            }while(opcao != EnumMenuPrincipal.SAIR);
            System.exit(0);
    }
    
    private static EnumMenuPrincipal exibirMenu(){
        EnumMenuPrincipal opcao;
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
        opcao = (EnumMenuPrincipal)JOptionPane.showInputDialog(null, "Escolha uma Opção", "Menu", JOptionPane.QUESTION_MESSAGE, null, EnumMenuPrincipal.values(), EnumMenuPrincipal.values()[0]);
        return opcao;
    }
}

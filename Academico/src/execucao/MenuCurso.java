package execucao;

import static execucao.Principal.menuPrincipal;
import persistencia.ConexaoBD;
import negocio.CursoNegocio;
import negocio.NegocioException;
import persistencia.PersistenciaException;
import vo.CursoVO;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/**
 * Menu responsavel pela parte curso
 * 
 * @author Eduardo Gonçalves da Silva
 */
public class MenuCurso {
    private static CursoNegocio cursoNegocio;
    
    public static void menuCurso() throws PersistenciaException{
        try{
            cursoNegocio =new CursoNegocio();
        }catch(NegocioException ex){
            System.out.println("Camada de negocio e persistencia não iniciada: " + ex);
        }
        if(cursoNegocio != null){
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
        cursoNegocio.inserir(lerDados());
    }
    
    public static void alterar() throws NegocioException{
        CursoVO cursoVO = null;
        try{
            cursoVO = cursoNegocio.pesquisaCodigo(Integer.parseInt(JOptionPane.showInputDialog("Digite o codigo")));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Digitação inconsistente: " + e);
        }
        if(cursoVO != null){
            CursoVO cursoTemp = lerDados(cursoVO);
            cursoTemp.setCodigo(cursoVO.getCodigo()); 
            cursoNegocio.alterar(cursoTemp);
            JOptionPane.showMessageDialog(null, "Alterado com sucesso");
        }else{
            JOptionPane.showMessageDialog(null, "Nada foi encontrado");
        }
    }
    
    public static void excluir() throws NegocioException{
        CursoVO cursoVO = null;
        try{
            cursoVO = cursoNegocio.pesquisaCodigo(Integer.parseInt(JOptionPane.showInputDialog("Digite o codigo")));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Digitação inconsistente: " + e);
        }
        if(cursoVO != null){
            cursoNegocio.excluir(cursoVO.getCodigo());
        }else{
            JOptionPane.showMessageDialog(null, "Nada foi encontrado");
        }
    }
    
    public static void pesquisarNome() throws NegocioException{
        String nome = JOptionPane.showInputDialog("Digite o nome");
        if(nome != null){
            List<CursoVO> listaVO = cursoNegocio.pesquisaNome(nome);
            if(listaVO.isEmpty() != true){
                System.out.println(Arrays.toString(listaVO.toArray()));
            }else{
                JOptionPane.showMessageDialog(null, "Nada foi encontrado");
            }
        }else{
            JOptionPane.showMessageDialog(null, "Nome não pode ser Nulo");
        }
    }
    
    public static void pesquisarCodigo(){
        CursoVO cursoVO = null;
        try{
            cursoVO = cursoNegocio.pesquisaCodigo(Integer.parseInt(JOptionPane.showInputDialog("Digite o codigo")));
        }catch(Exception e){
            JOptionPane.showMessageDialog(null, "Digitação inconsistente: " + e);
        }
        if(cursoVO != null){
            System.out.println(cursoVO);
        }else{
            JOptionPane.showMessageDialog(null, "Nada foi encontrado");
        }
    }
    
    public static CursoVO lerDados(CursoVO cursoTemp){
      cursoTemp.setNome(JOptionPane.showInputDialog("Digite o nome",cursoTemp.getNome()));
      cursoTemp.setDescricao(JOptionPane.showInputDialog("Digite a Descrição",cursoTemp.getDescricao()));
      return cursoTemp;
    }
    
    public static CursoVO lerDados(){
        CursoVO cursoTemp = new CursoVO();
        return lerDados(cursoTemp);
    }   
    
    public static void recuperar() throws PersistenciaException{
        System.out.println(cursoNegocio.recuperar());
    }
    
    private static void listar() throws NegocioException{
        try {
            List<CursoVO> listaCurso = cursoNegocio.recuperar();
            int i =0;
            while(i<listaCurso.size()){
                System.out.println(listaCurso.get(i).getListar());
                i++;
            }            
        } catch (PersistenciaException ex) {
             throw new NegocioException("Erro na listagem: "+ex.getMessage());
        }
        
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

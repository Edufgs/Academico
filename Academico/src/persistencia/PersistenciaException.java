package persistencia;

/**
 * Faz o tratamento de erro de conexao
 * 
 * @author Eduardo Gonçalves da Silva
 */
public class PersistenciaException extends Exception {

    public PersistenciaException() {
        super("Erro ocorrido na manipulação do banco de dados");
    }

    public PersistenciaException(String string) {
        super(string);
    }
}

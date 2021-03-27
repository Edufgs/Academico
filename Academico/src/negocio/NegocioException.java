package negocio;

/**
 * Tratamento de erro na camada negocio
 * 
 * @author Eduardo Gonçalves da Silva
 */
public class NegocioException extends Exception {

    public NegocioException(){
        super("Erro ocorrido na camada de negocio");
    }

    public NegocioException(String string) {
        super(string);
    }
    
}

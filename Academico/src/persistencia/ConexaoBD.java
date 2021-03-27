package persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * Classe Responsavel pela conexao com o banco
 * 
 * @author Eduardo Gonçalves da Silva
 */
public class ConexaoBD {
    private Connection conexao;
    private static ConexaoBD instancia; //vai guardar a conexao para ser usada em outras classes
    
    private ConexaoBD() throws PersistenciaException{
        Connection con = null;  
        try{//tratamento de erro se o banco não abrir
            String URL = "jdbc:postgresql://localhost:5432/academico"; //URL de conexao com o banco postgresql
            String DRIVE = "org.postgresql.Driver"; //Carregamento do drive, fazendo a carga.
            String USER = "postgres"; //Usuario
            String PASS = "postgres"; //Senha
            Class.forName(DRIVE); //Carregamento do drive, fazendo a carga
            this.conexao = DriverManager.getConnection(URL, USER, PASS); //pedindo a conexao passando a URL, Usuario e senha
        }catch(ClassNotFoundException e){ //se falha a carga do drive
            throw new PersistenciaException("Drive do banco de dados não localizado: " + e);
        }catch(SQLException e){//se falhar a conexao
            throw new PersistenciaException("Erro ao conectar o banco de dados: " + e);
        }
    }
    
    public static ConexaoBD getInstacia() throws PersistenciaException{
        if(instancia == null){
            instancia = new ConexaoBD();
        }
        return instancia;
    }
   
    public void desconectar() throws PersistenciaException{
         try{
            conexao.close();
            instancia = null;
            }catch(SQLException e){
                throw new PersistenciaException("Erro ao desconectar do banco de dados: " + e);
            }
    }

    public Connection getConexao() {
        return conexao;
    }
}

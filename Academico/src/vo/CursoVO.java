package vo;

/**
 * Usado para transporte de dados entre as camadas.
 * @author Eduardo Gonçalves da Silva
 */
public class CursoVO {
    private int codigo;
    private String nome;
    private String descricao;

    public CursoVO() {
    }
       
    public CursoVO(int codigo,String nome, String descricao) {
        this.nome = nome;
        this.descricao = descricao;
    }
    
    public int getCodigo() {
        return codigo;
    }

    public void setCodigo(int codigo) {
        this.codigo = codigo;
    }
    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
    
    public String getListar(){
        return "\nCursoVO{ " + "codigo=" + codigo + ", nome=" + nome + '}';
    }
    
    @Override
    public String toString() {
        return "\nCursoVO{" + "codigo=" + codigo + ", nome=" + nome + ", descricao=" + descricao + '}';
    }
}

package vo;

import java.util.ArrayList;
import java.util.List;

public class AlunoVO {

    private int matricula;
    private String nome;
    private String nomeMae;
    private String nomePai;
    private EnumSexo sexo;
    private EnderecoVO endereco;
    private CursoVO curso;
    private List<DisciplinaVO> disciplinas;

    public AlunoVO() {
        this.endereco = new EnderecoVO();
        this.curso = new CursoVO();
        this.disciplinas = new ArrayList();
    }

    public AlunoVO(int matricula, String nome, String nomeMae, String nomePai, EnumSexo sexo, EnderecoVO endereco, CursoVO curso, List<DisciplinaVO> disciplinas) {
        this.matricula = matricula;
        this.nome = nome;
        this.nomeMae = nomeMae;
        this.nomePai = nomePai;
        this.sexo = sexo;
        this.endereco = endereco;
        this.curso = curso;
        this.disciplinas = disciplinas;
    }

    public List<DisciplinaVO> getDisciplinas() {
        return disciplinas;
    }

    public void setDisciplinas(List<DisciplinaVO> disciplinas) {
        this.disciplinas = disciplinas;
    }
    
    public int getMatricula() {
        return matricula;
    }

    public void setMatricula(int matricula) {
        this.matricula = matricula;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getNomeMae() {
        return nomeMae;
    }

    public void setNomeMae(String nomeMae) {
        this.nomeMae = nomeMae;
    }

    public String getNomePai() {
        return nomePai;
    }

    public void setNomePai(String nomePai) {
        this.nomePai = nomePai;
    }

    public EnumSexo getSexo() {
        return sexo;
    }

    public void setSexo(EnumSexo sexo) {
        this.sexo = sexo;
    }

    public EnderecoVO getEndereco() {
        return endereco;
    }

    public void setEndereco(EnderecoVO endereco) {
        this.endereco = endereco;
    }

    public CursoVO getCurso() {
        return curso;
    }

    public void setCurso(CursoVO curso) {
        this.curso = curso;
    }

    public String getListar(){
        return "\nAlunoVO{ " + "Matricula= " + matricula + ", Nome= " + nome + ", Sexo= " + sexo + '}';
    }

    @Override
    public String toString() {
        return "\nAlunoVO{" + "matricula= " + matricula + ", nome= " + nome + ", nomeMae= " + nomeMae + ", nomePai= " + nomePai + ", sexo= " + sexo + ", endereco= " + endereco + ", curso= " + curso + ", disciplinas= " + disciplinas + '}';
    }
}

package vo;

import java.util.List;

/**
 *  Usado para transporte de dados entre as camadas.
 * @author Eduardo Gonçalves da Silva
 */
public class DisciplinaVO {
    private int codigo;
    private String nome;
    private int semestre;
    private int cargaHoraria;
    private CursoVO curso;
    
    public DisciplinaVO() {
        this.curso = new CursoVO();
    }

    public DisciplinaVO(int codigo, String nome, int semestre, int cargaHoraria, CursoVO curso, List<AlunoVO> alunos) {
        this.codigo = codigo;
        this.nome = nome;
        this.semestre = semestre;
        this.cargaHoraria = cargaHoraria;
        this.curso = curso;
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

    public int getSemestre() {
        return semestre;
    }

    public void setSemestre(int semestre) {
        this.semestre = semestre;
    }

    public int getCargaHoraria() {
        return cargaHoraria;
    }

    public void setCargaHoraria(int cargaHoraria) {
        this.cargaHoraria = cargaHoraria;
    }

    public CursoVO getCurso() {
        return curso;
    }

    public void setCurso(CursoVO curso) {
        this.curso = curso;
    }

    public String getListar(){
        return "\nDisciplina {" +" codigo= " + codigo + ", nome= " + nome + ", semestre= " + semestre + ", cargaHoraria= " + cargaHoraria + '}';
    }

    @Override
    public String toString() {
        return "\nDisciplinaVO{" + "codigo= " + codigo + ", nome= " + nome + ", semestre= " + semestre + ", cargaHoraria= " + cargaHoraria + ", curso= " + curso + '}';
    }
}

public class Revista extends ItemDoAcervo{

  private int edicao;

  public Revista(String titulo, int ano, int edicao) {
    super(titulo, ano);
    this.edicao = edicao;

  }

  @Override
  public String toString() {
    return "Revista{" +
      "ano=" + getAno() + '\'' +
      ", titulo='" + getTitulo() + '\'' +
      ", edicao=" +   edicao  +
      '}';

  }
}

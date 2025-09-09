public class Revista extends ItemDoAcervo{

  private int edicao;

  public Revista(String titulo, int ano, int edicao) {
    super(titulo, ano);
    this.edicao = edicao;

  }

  @Override
  public int getPrazo() {
    return 7;
  }

  @Override
  public double getValorMultaPorDia() {
    return 1;
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

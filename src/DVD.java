public class DVD extends ItemDoAcervo{
  private int duracaoMinutos;

  //getters
  public int getDuracaoMinutos() {
    return duracaoMinutos;
  }

  @Override
  public int getPrazo(){
    return 3;
  }

  @Override
  public double getValorMultaPorDia(){
    return 2;
  }
  //setters
  public void setDuracaoMinutos(int duracaoMinutos) {
    this.duracaoMinutos = duracaoMinutos;
  }

  public DVD(String titulo, int ano, int duracaoMinutos) {
    super(titulo, ano);
    setDuracaoMinutos(duracaoMinutos);
  }

  @Override
  public String toString() {
    return "DVD{" + "'" +getTitulo()+ "' ," + " de (" + getAno() + ")" + " - " + duracaoMinutos + "min" + " - Status: " + getStatus() + '}';
  }
}

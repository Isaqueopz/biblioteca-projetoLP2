public class Livro extends ItemDoAcervo {
  private String autor;

  //Setter
  public void setAutor(String autor) {
    if (autor == "") {
      System.out.println("ERRO: autor invalido.");
    }else{
      this.autor = autor;
    }
  }
  //Getter
  public String getAutor() {
    return autor;
  }

  //Construtor
  public Livro(String titulo, String autor, int ano){
    super(titulo,ano);
    this.autor = autor;
  }

  @Override
  public String toString() {
    return "Livro '" + getTitulo() + "', de " + getAutor() + " (" + getAno() + ") - Status: " + getStatus();
  }



}

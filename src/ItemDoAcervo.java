public class ItemDoAcervo {

  private String titulo;
  private int ano;
  private StatusLivro status;


  //SETTERS
  public void setTitulo(String titulo) {
    if (titulo == "") {
      System.out.println("ERRO: titulo invalido.");
    }else{
      this.titulo = titulo;
    }
  }
  public void setStatus(StatusLivro status) {
    this.status = status;
  }
  public void setAno(int ano) {
    int ano_atual = 2025;
    if (ano>ano_atual) {
      System.out.println("ERRO: ano invalido.");
    }else {
      this.ano = ano;
    }
  }


  //GETTERS
  public String getTitulo() {
    return titulo;
  }
  public StatusLivro getStatus() {
    return status;
  }
  public int getAno() {
    return ano;
  }

  public ItemDoAcervo(String titulo, int ano) {
    setTitulo(titulo);
    setAno(ano);
    setStatus(StatusLivro.DISPONIVEL);
  }



}

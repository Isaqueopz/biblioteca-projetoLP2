import java.time.LocalDate;

public class Emprestimo {
  private ItemDoAcervo item;
  private Usuario usuario;
  private LocalDate dataEmprestimo;
  private LocalDate dataDevolucaoPrevista;
  private LocalDate dataDevolucaoReal;

  //Getters

  public ItemDoAcervo getItem() {
    return item;
  }

  public Usuario getUsuario() {
    return usuario;
  }
  public LocalDate getDataEmprestimo() {
    return dataEmprestimo;
  }
  public LocalDate getDataDevolucaoPrevista() {
    return dataDevolucaoPrevista;
  }
  public LocalDate getDataDevolucaoReal() {
    return dataDevolucaoReal;
  }

  //Setters

  public void setItem(ItemDoAcervo item) {
    this.item = item;
  }

  public void setUsuario(Usuario usuario) {
    this.usuario = usuario;
  }
  public void setDataEmprestimo(LocalDate dataEmprestimo) {
    this.dataEmprestimo = dataEmprestimo;
  }
  public void setDataDevolucaoPrevista(LocalDate dataDevolucaoPrevista) {
    this.dataDevolucaoPrevista = dataDevolucaoPrevista;
  }
  public void setDataDevolucaoReal(LocalDate dataDevolucaoReal) {
    this.dataDevolucaoReal = dataDevolucaoReal;
  }


  public Emprestimo(ItemDoAcervo item, Usuario usuario, LocalDate dataEmprestimo, LocalDate dataDevolucaoPrevista) {
    setItem(item);
    setUsuario(usuario);
    setDataEmprestimo(dataEmprestimo);
    setDataDevolucaoPrevista(dataDevolucaoPrevista);
    this.dataDevolucaoReal = null;
  }  // construtor


  @Override
  public String toString() {
    return "Emprestimo{" +
      "Item=" + item.getTitulo() +
      ", usuario=" + usuario.getName() +
      ", dataEmprestimo=" + dataEmprestimo +
      ", dataDevolucaoPrevista=" + dataDevolucaoPrevista +
      '}';
  }
}




import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

public class Biblioteca {

  private List<ItemDoAcervo> acervo;
  private List<Usuario> listaDeUsuarios;
  private List<Emprestimo> registroDeEmprestimos;

  public Biblioteca() {
    this.acervo = new ArrayList<>();
    this.listaDeUsuarios = new ArrayList<>();
    this.registroDeEmprestimos = new ArrayList<>();
  }

  // ------------------ BUSINESS LOGIC ------------------

  public boolean realizarEmprestimo(String idUsuario, String titulo) {
    Optional<Usuario> usuarioOpt = pesquisarUsuarioPorId(idUsuario);
    if (usuarioOpt.isEmpty()) {
      System.out.println("Error: this user is not registered.");
      return false;
    }
    Usuario usuario = usuarioOpt.get();

    Optional<ItemDoAcervo> itemOpt = pesquisarItemPorTitulo(titulo);
    if (itemOpt.isEmpty()) {
      System.out.println("Error: this item is not registered.");
      return false;
    }
    ItemDoAcervo item = itemOpt.get();

    if (!itemDisponivel(item)) {
      System.out.println("Error: this item is already loaned.");
      return false;
    }

    item.setStatus(StatusLivro.EMPRESTADO); // ou StatusItem se generalizar
    LocalDate hoje = LocalDate.now();
    LocalDate devolucaoPrevista = hoje.plusDays(item.getPrazo()); // usar prazo do item
    Emprestimo emprestimo = new Emprestimo(item, usuario, hoje, devolucaoPrevista);
    registroDeEmprestimos.add(emprestimo);

    System.out.println("Loan successfully registered.");
    return true;
  }

  private boolean itemDisponivel(ItemDoAcervo item) {
    return item.getStatus() == StatusLivro.DISPONIVEL;
  }

  // ------------------ SEARCHES ------------------

  public Optional<ItemDoAcervo> pesquisarItemPorTitulo(String titulo) {
    return acervo.stream()
      .filter(i -> i.getTitulo().equalsIgnoreCase(titulo))
      .findFirst();
  }

  public Optional<Usuario> pesquisarUsuarioPorId(String id) {
    return listaDeUsuarios.stream()
      .filter(u -> u.getId().equals(id))
      .findFirst();
  }

  public Emprestimo buscarEmprestimoAtivoPorItem(ItemDoAcervo item) {
    return registroDeEmprestimos.stream()
      .filter(e -> e.getItem().equals(item) && e.getDataDevolucaoReal() == null)
      .findFirst()
      .orElse(null);
  }

  // ------------------ REGISTRATIONS ------------------

  public boolean cadastrarItem(ItemDoAcervo item) {
    if (acervo.contains(item)) {
      System.out.println("Esse item já está no acervo.");
      return false;
    }
    acervo.add(item);
    System.out.println("Item \"" + item.getTitulo() + "\" adicionado no acervo.");
    return true;
  }

  public boolean cadastrarUsuario(Usuario usuario) {
    if (listaDeUsuarios.contains(usuario)) {
      System.out.println("This user is already registered.");
      return false;
    }
    listaDeUsuarios.add(usuario);
    System.out.println("The user " + usuario.getName() + " has been registered.");
    return true;
  }

  public void listarAcervo() {
    System.out.println("\n📚 Items in the Collection:");
    if (acervo.isEmpty()) {
      System.out.println("No items registered.");
    } else {
      acervo.forEach(System.out::println);
    }
  }

  public void realizarDevolucao(String titulo) {
    Optional<ItemDoAcervo> itemOpt = pesquisarItemPorTitulo(titulo);

    if (itemOpt.isEmpty()) {
      System.out.println("Error: this item is not registered.");
      return;
    }

    ItemDoAcervo item = itemOpt.get();
    Emprestimo emprestimo = buscarEmprestimoAtivoPorItem(item);

    if (emprestimo == null) {
      System.out.println("Error: this loan does not exist.");
      return;
    }

    LocalDate hoje = LocalDate.now();
    long diasAtraso = ChronoUnit.DAYS.between(emprestimo.getDataDevolucaoPrevista(), hoje);

    if (diasAtraso > 0) {
      double multa = diasAtraso * item.getValorMultaPorDia();
      System.out.println("Item returned. You need to pay a fine of R$" + multa);
    } else {
      System.out.println("Item returned.");
    }

    item.setStatus(StatusLivro.DISPONIVEL);
    emprestimo.setDataDevolucaoReal(hoje);
  }

  // ------------------ MAIN ------------------

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    boolean opcao_exit = false;
    Biblioteca library = new Biblioteca();

    while (!opcao_exit) {
      System.out.println("\n================Library================\n");
      System.out.println("Welcome to the central library system!\n");
      System.out.println("Please choose an option:\n");
      System.out.println("[1] REGISTER A NEW ITEM");
      System.out.println("[2] REGISTER A NEW USER");
      System.out.println("[3] SEARCH ITEMS");
      System.out.println("[4] MAKE A LOAN");
      System.out.println("[5] VIEW LIBRARY COLLECTION");
      System.out.println("[6] RETURN AN ITEM");
      System.out.println("[7] EXIT MENU");
      int opcao = sc.nextInt();
      sc.nextLine();

      switch (opcao) {
        case 1:
          System.out.println("Enter the title of the item:");
          String titulo = sc.nextLine();
          System.out.println("Enter the author/creator:");
          String autor = sc.nextLine();
          System.out.println("Enter the year:");
          int ano = sc.nextInt();
          sc.nextLine();

          boolean exists = library.acervo.stream()
            .anyMatch(i -> i.getTitulo().equalsIgnoreCase(titulo) && i.getTitulo().equalsIgnoreCase(autor));

          if (exists) {
            System.out.println("This item is already registered.");
          } else {
            library.cadastrarItem(new Livro(titulo, autor, ano)); // ainda pode ser Livro, DVD etc.
            System.out.println("Item successfully registered!");
          }
          break;

        case 2:
          System.out.println("Enter the name of the user:");
          String nomeUser = sc.nextLine();
          System.out.println("Enter the user ID:");
          String idUser = sc.nextLine();

          boolean userExists = library.listaDeUsuarios.stream()
            .anyMatch(u -> u.getName().equalsIgnoreCase(nomeUser));

          if (userExists) {
            System.out.println("This user is already registered.");
          } else {
            library.cadastrarUsuario(new Usuario(nomeUser, idUser));
            System.out.println("User successfully registered!");
          }
          break;

        case 3:
          System.out.println("Enter the title to search:");
          String searchTitle = sc.nextLine();
          Optional<ItemDoAcervo> found = library.pesquisarItemPorTitulo(searchTitle);
          System.out.println(found.map(i -> "Item found:\n" + i).orElse("Item not found."));
          break;

        case 4:
          System.out.println("Enter the user ID:");
          String userIdLoan = sc.nextLine();
          System.out.println("Enter the item title:");
          String itemTitleLoan = sc.nextLine();
          library.realizarEmprestimo(userIdLoan, itemTitleLoan);
          break;

        case 5:
          library.listarAcervo();
          break;

        case 6:
          System.out.println("Enter the title of the item to return:");
          String titleToReturn = sc.nextLine();
          library.realizarDevolucao(titleToReturn);
          break;

        case 7:
          System.out.println("You have exited the library system.");
          opcao_exit = true;
          break;

        default:
          System.out.println("Invalid option.");
      }
    }
  }
}

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
  private static final int PRAZO_EMPRESTIMO_DIAS = 14;
  private static final double VALOR_MULTA_POR_DIA = 0.75;

  public Biblioteca() {
    this.acervo = new ArrayList<>();
    this.listaDeUsuarios = new ArrayList<>();
    this.registroDeEmprestimos = new ArrayList<>();
  }

  // ------------------ BUSINESS LOGIC ------------------

  public boolean realizarEmprestimo(String idUsuario, String titulo) {
    Usuario usuarioDoEmprestimo = pesquisarUsuarioPorId(idUsuario)
      .orElse(null);

    if (usuarioDoEmprestimo == null) {
      System.out.println("Error: this user is not registered.");
      return false;
    }

    Optional<Livro> livroOpt = pesquisarLivroPorTitulo(titulo);
    if (livroOpt.isEmpty()) {
      System.out.println("Error: this book is not registered.");
      return false;
    }

    Livro livro = livroOpt.get();

    if (!livroDisponivel(livro)) {
      System.out.println("Error: this book is already loaned.");
      return false;
    }

    livro.setStatus(StatusLivro.EMPRESTADO);
    LocalDate hoje = LocalDate.now();
    LocalDate devolucaoPrevista = hoje.plusDays(PRAZO_EMPRESTIMO_DIAS);
    Emprestimo emprestimo = new Emprestimo(livro, usuarioDoEmprestimo, hoje, devolucaoPrevista);
    registroDeEmprestimos.add(emprestimo);

    System.out.println("Loan successfully registered.");
    return true;
  }

  private boolean livroDisponivel(Livro livro) {
    return livro.getStatus() == StatusLivro.DISPONIVEL;
  }

  // ------------------ SEARCHES ------------------ //

  public Optional<Livro> pesquisarLivroPorTitulo(String titulo) {
    return acervo.stream()
      .filter(item -> item instanceof Livro)
      .map(item -> (Livro) item)
      .filter(l -> l.getTitulo().equalsIgnoreCase(titulo))
      .findFirst();
  }

  public Optional<Usuario> pesquisarUsuarioPorId(String id) {
    return listaDeUsuarios.stream()
      .filter(u -> u.getId().equals(id))
      .findFirst();
  }

  public Emprestimo buscarEmprestimoAtivoPorLivro(Livro livro) {
    for (Emprestimo emprestimo : registroDeEmprestimos) {
      if (emprestimo.getItem().getTitulo().equalsIgnoreCase(livro.getTitulo())) {
        if (emprestimo.getDataDevolucaoReal() == null) {
          return emprestimo;
        }
      }
    }
    return null;
  }

  // ------------------ REGISTRATIONS ------------------ //

  public boolean cadastrarItem(ItemDoAcervo item) {
    if (acervo.contains(item)) {
      System.out.println("Esse item já está no acervo.");
      return false;
    }
    this.acervo.add(item);
    System.out.println("Livro \"" + item.getTitulo() + "\" adicionado no acervo.");
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
    System.out.println("\n📚 Books in the Collection:");
    if (acervo.isEmpty()) {
      System.out.println("No books registered.");
    } else {
      acervo.forEach(System.out::println);
    }
  }

  public void realizarDevolucao(String titulo) {
    Optional<Livro> livroOpt = pesquisarLivroPorTitulo(titulo);

    if (livroOpt.isEmpty()) {
      System.out.println("Error: this book is not registered.");
      return;
    }

    Livro livro = livroOpt.get();
    Emprestimo emprestimo = buscarEmprestimoAtivoPorLivro(livro);

    if (emprestimo == null) {
      System.out.println("Error: this loan does not exist.");
      return;
    }

    LocalDate hoje = LocalDate.now();
    long diasAtraso = ChronoUnit.DAYS.between(emprestimo.getDataDevolucaoPrevista(), hoje);

    if (diasAtraso > 0) {
      double multa = diasAtraso * VALOR_MULTA_POR_DIA;
      System.out.println("Book returned. You need to pay a fine of R$" + multa);
    } else {
      System.out.println("Book returned.");
    }

    emprestimo.getItem().setStatus(StatusLivro.DISPONIVEL);
    emprestimo.setDataDevolucaoReal(hoje);
  }

  // ------------------ MAIN ------------------ //

  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    boolean opcao_exit = false;
    Biblioteca library_01 = new Biblioteca();

    while (!opcao_exit) {
      System.out.println("\n================Library================\n");
      System.out.println("Welcome to the central library system!\n");
      System.out.println("Please choose an option:\n");
      System.out.println("[1] REGISTER A NEW BOOK");
      System.out.println("[2] REGISTER A NEW USER");
      System.out.println("[3] SEARCH BOOKS");
      System.out.println("[4] MAKE A LOAN");
      System.out.println("[5] VIEW LIBRARY COLLECTION");
      System.out.println("[6] RETURN A BOOK");
      System.out.println("[7] EXIT MENU");
      int opcao = sc.nextInt();
      sc.nextLine();

      switch (opcao) {
        case 1:
          System.out.println("Enter the name of the book:");
          String nameBook = sc.nextLine();

          System.out.println("Enter the author's name:");
          String authorName = sc.nextLine();

          System.out.println("Enter the year of the book:");
          int yearBook = sc.nextInt();
          sc.nextLine();

          boolean exists = library_01.acervo.stream()
            .filter(item -> item instanceof Livro)
            .map(item -> (Livro) item)
            .anyMatch(l -> l.getTitulo().equalsIgnoreCase(nameBook)
              && l.getAutor().equalsIgnoreCase(authorName));

          if (exists) {
            System.out.println("This book is already registered in the library.");
          } else {
            library_01.cadastrarItem(new Livro(nameBook, authorName, yearBook));
            System.out.println("Book successfully registered!");
          }
          break;

        case 2:
          System.out.println("Enter the name of the user:");
          String nameUser = sc.nextLine();
          System.out.println("Enter the user ID:");
          String idUser = sc.nextLine();

          boolean user_exists = library_01.listaDeUsuarios.stream()
            .anyMatch(n -> n.getName().equalsIgnoreCase(nameUser));

          if (user_exists) {
            System.out.println("This user is already registered in the library.");
          } else {
            library_01.cadastrarUsuario(new Usuario(nameUser, idUser));
            System.out.println("User successfully registered!");
          }
          break;

        case 3:
          System.out.println("Enter the title to search:");
          String titleSearch = sc.nextLine();
          Optional<Livro> found = library_01.pesquisarLivroPorTitulo(titleSearch);
          if (found.isPresent()) {
            System.out.println("Book found:\n" + found.get());
          } else {
            System.out.println("Book not found.");
          }
          break;

        case 4:
          System.out.println("Enter the user ID:");
          String userIdLoan = sc.nextLine();
          System.out.println("Enter the book title:");
          String bookTitleLoan = sc.nextLine();
          library_01.realizarEmprestimo(userIdLoan, bookTitleLoan);
          break;

        case 5:
          library_01.listarAcervo();
          break;

        case 6:
          System.out.println("Enter the title of the book to return:");
          String titleToReturn = sc.nextLine();
          library_01.realizarDevolucao(titleToReturn);
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

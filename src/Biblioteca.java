// Vale salientar que muita coisa do código está diferente do senhor, entretanto toda a lógica do código do senhor, está aqui!
//Resposta ex:0:
//
//   Com a classe 'ItemDoAcervo', o código funcionaria, criando um objeto "genérico",
//   mas este objeto seria incompleto, não tendo atributos específicos como 'autor' (de Livro),
//   isso levaria a inconsistências e possíveis erros na lógica de negócio, como no cálculo de prazos e multas.
//
//   No mundo real, um item de uma biblioteca é sempre algo específico, um Livro, uma Revista, um DVD, etc.

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
  public List<ItemDoAcervo> buscar(String termo) {
    String termoBusca = termo.toLowerCase();
    return acervo.stream().filter(item -> {
      boolean tituloContemTermo = item.getTitulo().toLowerCase().contains(termoBusca);
      if (tituloContemTermo) {
        return true;
      }
      if (item instanceof Livro) {
        Livro livro = (Livro) item;
        boolean autorContemTermo = livro.getAutor().toLowerCase().contains(termoBusca);
        return autorContemTermo;
      }
      return false;
    }).toList();
  }

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

    System.out.println("Loan successfully registered. " + "Expected return date: " + devolucaoPrevista);

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
          System.out.println("\nQual tipo de item deseja cadastrar?");
          System.out.println("[1] Livro");
          System.out.println("[2] DVD");
          System.out.println("[3] Revista");
          int tipoItem = sc.nextInt();
          sc.nextLine();

          ItemDoAcervo novoItem = null;



          switch (tipoItem) {
            case 1: {
              System.out.println("--- Cadastro de Livro ---");
              System.out.println("Digite o título:");
              String tituloLivro = sc.nextLine();
              System.out.println("Digite o autor:");
              String autorLivro = sc.nextLine();
              System.out.println("Digite o ano:");
              int anoLivro = sc.nextInt();
              sc.nextLine();


              novoItem = new Livro(tituloLivro, autorLivro, anoLivro);
              break;
            }
            case 2: {
              System.out.println("--- Cadastro de DVD ---");
              System.out.println("Digite o título:");
              String tituloDvd = sc.nextLine();
              System.out.println("Digite a duração em minutos:");
              int duracaoDvd = sc.nextInt();
              sc.nextLine();
              System.out.println("Digite o ano:");
              int anoDvd = sc.nextInt();
              sc.nextLine();


              novoItem = new DVD(tituloDvd, anoDvd, duracaoDvd);
              break;
            }
            case 3: {
              System.out.println("--- Cadastro de Revista ---");
              System.out.println("Digite o título:");
              String tituloRevista = sc.nextLine();
              System.out.println("Digite a edição:");
              int edicaoRevista = sc.nextInt();
              sc.nextLine();
              System.out.println("Digite o ano:");
              int anoRevista = sc.nextInt();
              sc.nextLine();


              novoItem = new Revista(tituloRevista, anoRevista, edicaoRevista);
              break;
            }
            default:
              System.out.println("Opção de item inválida.");
              break;
          }


          if (novoItem != null) {
            ItemDoAcervo finalNovoItem = novoItem;
            boolean exists = library.acervo.stream()
              .anyMatch(i -> i.getTitulo().equalsIgnoreCase(finalNovoItem.getTitulo()));

            if (exists) {
              System.out.println("Erro: Um item com este título já está cadastrado.");
            } else {
              library.cadastrarItem(novoItem);
            }
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
          System.out.println("Digite o termo para buscar (título ou autor):");
          String termo = sc.nextLine();
          List<ItemDoAcervo> resultados = library.buscar(termo);
          if (resultados.isEmpty()) {
            System.out.println("Nenhum item encontrado com o termo '" + termo + "'.");
          } else {
            System.out.println("\nResultados da busca (" + resultados.size() + " encontrados):");
            resultados.forEach(System.out::println);
          }
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

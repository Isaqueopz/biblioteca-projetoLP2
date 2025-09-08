import java.util.Objects;

public class Usuario {
  private String name;
  private String id;


  //Getters
  public String getName() {
    return name;
  }
  public String getId() {
    return id;
  }

  //Setters
  public void setName(String name) {
    if (name == null || name.equals("")) {
      setName("ERROR: Nome não pode ser vazio.");
    }
    else  {
      this.name = name;
    }
  }
  public void setId(String id) {
    if (id == null || id.equals("")) {
      setId("NULL");
    } else {
      this.id = id;
    }
  }

  public Usuario(String name, String id) {
    setName(name);
    setId(id);
  } // construtor

  @Override
  public String toString() {
    return "Usuario{" +
      "nome='" + name + '\'' +
       + '\'' +
      '}';
  }
  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    Usuario usuario = (Usuario) o;
    return Objects.equals(id, usuario.id);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}

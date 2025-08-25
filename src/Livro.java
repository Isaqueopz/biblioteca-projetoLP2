public class Livro {
  private String title;
  private String author;
  private String publisher;
  private String gender;
  private int numberPages;
  private int yearPublished;

  public String getTitle() {
    return this.title;
  }
  public void setTitle(String title) {
    if (title == null || title.isEmpty()) {
      this.title = "Livro sem nome";
    } else {
      this.title = title;
    }
  }

  public String getAuthor() {
    return this.author;
  }
  public void setAuthor(String author) {
    if (author == null || author.isEmpty()) {
      this.author = "Autor sem nome";
    } else {
      this.author = author;
    }
  }

  public String getPublisher() {
    return this.publisher;
  }
  public void setPublisher(String publisher) {
    if (publisher == null || publisher.isEmpty()) {
      this.publisher = "Livro sem informação da editora";
    } else {
      this.publisher = publisher;
    }
  }

  public String getGender() {
    return this.gender;
  }
  public void setGender(String gender) {
    if (gender == null || gender.isEmpty()) {
      this.gender = "Livro sem gênero definido";
    } else {
      this.gender = gender;
    }
  }

  public int getNumberPages() {
    return this.numberPages;
  }
  public void setNumberPages(int numberPages) {
    if (numberPages <= 0) {
      this.numberPages = 0;
    } else {
      this.numberPages = numberPages;
    }
  }

  public int getYearPublished() {
    return this.yearPublished;
  }
  public void setYearPublished(int yearPublished) {
    if (yearPublished < 0 || yearPublished > 2025) {
      this.yearPublished = 0;
    } else {
      this.yearPublished = yearPublished;
    }
  }

  public Livro(String title, String author, String publisher, String gender, int numberPages, int yearPublished) {
    setTitle(title);
    setAuthor(author);
    setPublisher(publisher);
    setGender(gender);
    setNumberPages(numberPages);
    setYearPublished(yearPublished);
  }

  public String toString() {
    return "Livro: " + this.title + ", " + this.author + ", " + this.publisher + ", "
      + this.gender + ", " + this.numberPages + ", " + this.yearPublished;
  }
}

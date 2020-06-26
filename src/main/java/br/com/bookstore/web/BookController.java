package br.com.bookstore.web;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.bookstore.model.BookEntity;
import br.com.bookstore.repository.BookRepository;

@CrossOrigin(origins = "*", allowedHeaders = "*")
@RestController
@RequestMapping("/books")
public class BookController {
	
	@Autowired
	private BookRepository repository;
	
	@GetMapping("/hello")
	public String getApp() {
		return "Spring Boot Application";
	}
	
	@GetMapping("/")
	public List<BookEntity> getAllBooks() {
		return this.repository.findAll();
	}
	
	public BookEntity createBook(@Valid @RequestBody BookEntity book) {
		return this.repository.save(book);
	}

}

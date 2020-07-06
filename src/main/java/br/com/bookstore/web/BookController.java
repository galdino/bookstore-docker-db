package br.com.bookstore.web;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
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
	public ResponseEntity<String> getApp() {
		return new ResponseEntity<String>("Spring Boot Application", HttpStatus.OK);
	}
	
	@GetMapping("/")
	public ResponseEntity<List<BookEntity>> getAllBooks() {
		List<BookEntity> bookEntityList = this.repository.findAll();
		if(bookEntityList.isEmpty()){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			return new ResponseEntity<List<BookEntity>>(bookEntityList, HttpStatus.OK);
		}
	}
	
	@GetMapping("/{serialNumber}")
	public ResponseEntity<BookEntity> getOneBook(@PathVariable() Long serialNumber) {
		Optional<BookEntity> bookEntityO = this.repository.findById(serialNumber);
		if(bookEntityO.isPresent()){
			return new ResponseEntity<BookEntity>(bookEntityO.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping("/")
	public ResponseEntity<BookEntity> createBook(@Valid @RequestBody BookEntity book) {
		return new ResponseEntity<BookEntity>(this.repository.save(book), HttpStatus.CREATED);
	}

}

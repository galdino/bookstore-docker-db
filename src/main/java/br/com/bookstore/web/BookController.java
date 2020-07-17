package br.com.bookstore.web;

import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
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
	
	@GetMapping
	public ResponseEntity<List<BookEntity>> getAllBooks() {
		List<BookEntity> bookEntityList = this.repository.findAllByOrderBySerialNumberAsc();
		if(bookEntityList.isEmpty()){
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		} else {
			for (BookEntity bookEntity : bookEntityList) {
				Long serialNumber = bookEntity.getSerialNumber();
				bookEntity.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(BookController.class).getOneBook(serialNumber)).withSelfRel());
			}
			return new ResponseEntity<List<BookEntity>>(bookEntityList, HttpStatus.OK);
		}
	}
	
	@GetMapping("/{serialNumber}")
	public ResponseEntity<BookEntity> getOneBook(@PathVariable(value="serialNumber") long serialNumber) {
		Optional<BookEntity> bookEntityO = this.repository.findById(serialNumber);
		if(bookEntityO.isPresent()){
			bookEntityO.get().add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(BookController.class).getAllBooks()).withRel("List of Books"));
			return new ResponseEntity<BookEntity>(bookEntityO.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}
	
	@PostMapping
	public ResponseEntity<BookEntity> createBook(@Valid @RequestBody BookEntity book) {
		BookEntity bookEntity = this.repository.save(book);
		bookEntity.add(ControllerLinkBuilder.linkTo(ControllerLinkBuilder.methodOn(BookController.class).getAllBooks()).withRel("List of Books"));
		return new ResponseEntity<BookEntity>(this.repository.save(book), HttpStatus.CREATED);
	}
	
	@DeleteMapping("/{serialNumber}")
	public ResponseEntity<BookEntity> deleteBook(@PathVariable(value="serialNumber") long serialNumber){
		ResponseEntity<BookEntity> responseEntityAux = this.getOneBook(serialNumber);
		if(responseEntityAux.getStatusCode().equals(HttpStatus.OK)){
			this.repository.deleteById(serialNumber);
			return new ResponseEntity<>(HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
	}
	
	@PutMapping("/{serialNumber}")
	public ResponseEntity<BookEntity> updateBook(@PathVariable(value="serialNumber") long serialNumber,
												 @Valid @RequestBody BookEntity book){
		ResponseEntity<BookEntity> responseEntityAux = this.getOneBook(serialNumber);
		if(responseEntityAux.getStatusCode().equals(HttpStatus.OK)){
			book.setSerialNumber(serialNumber);
			return new ResponseEntity<BookEntity>(this.repository.save(book), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

}

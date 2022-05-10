package acc.spring.security.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import acc.spring.security.model.Dog;

@RestController
@RequestMapping("/api/dog")
public class DogController {
	
	private List<Dog> dogsList = Arrays.asList(
			new Dog(1,"Manaseses",5),
			new Dog(2,"Babitas",10),
			new Dog(3,"Cuicochas",8)
	);
	
	@GetMapping("/{id}")
	public Dog getDog(@PathVariable("id") Integer dogId) {
		return dogsList.stream()
				.filter(doggo -> doggo.getId().equals(dogId))
				.findFirst()
				.orElseThrow(
						()->new IllegalStateException("Doggo with Id: "+dogId+" doesnt exist")
				);
	}
}

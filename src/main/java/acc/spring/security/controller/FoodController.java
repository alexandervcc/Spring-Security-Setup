package acc.spring.security.controller;

import acc.spring.security.model.Food;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import static acc.spring.security.security.UserRole.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/api/food")
public class FoodController {
    private ArrayList<Food> foodList = new ArrayList<Food>();

    {
        foodList.add( new Food(1,"NutraPro",9.00));
        foodList.add(new Food(2,"Vital Therapy",33.00));
        foodList.add(new Food(3,"Royal Cani",46.00));
    }

    @GetMapping({"/",""})
    @PreAuthorize("hasAnyRole('ROLE_DOGGERINO','ROLE_SUPER_DOG')")
    public List<Food> getFoodList(){
        return this.foodList;
    }

    @PostMapping("/new")
    @PreAuthorize("hasAnyAuthority('food:write')")
    public void addNewFood(@RequestBody Food newFood){
        System.out.println("Food: "+newFood.toString());
        try {
            this.foodList.add(newFood);
        }catch (Exception e){
            System.out.println("error: "+e.getMessage());
        }

    }

    @DeleteMapping("/{foodId}")
    @PreAuthorize("hasAnyAuthority('food:write')")
    public Integer deleteFood(@PathVariable("foodId") Integer idFood){
        return idFood;
    }

    @PutMapping("/{foodId}")
    @PreAuthorize("hasAnyAuthority('food:write')")
    public String updateFood(
            @PathVariable("foodId") Integer foodId ,
            @RequestBody Food updatedFood
    ){
        for(int i =0;i<this.foodList.size();i++){
            if(this.foodList.get(i).getId()==foodId){
                updatedFood.setId(foodId);
                this.foodList.set(i,updatedFood);
                return "Found and Updated";
            }
        }
        return "Not Found";
    }


}

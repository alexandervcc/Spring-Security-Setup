package acc.spring.security.controller;

import acc.spring.security.model.Food;
import org.springframework.web.bind.annotation.*;

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
    public List<Food> getFoodList(){
        return this.foodList;
    }

    @PostMapping("/new")
    public void addNewFood(@RequestBody Food newFood){
        System.out.println("Food: "+newFood.toString());
        try {
            this.foodList.add(newFood);
        }catch (Exception e){
            System.out.println("error: "+e.getMessage());
        }

    }

    @DeleteMapping("/{foodId}")
    public Integer deleteFood(@PathVariable("foodId") Integer idFood){
        return idFood;
    }

    @PutMapping("/{foodId}")
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

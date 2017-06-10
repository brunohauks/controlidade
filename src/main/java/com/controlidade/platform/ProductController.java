package com.controlidade.platform;

import com.controlidade.platform.entity.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.Instant;
import java.util.*;

@Controller
public class ProductController {

  @Autowired
  private ProductRepository repository;

  @RequestMapping(value = "/categories")
  public @ResponseBody
  List<CategoriesResponse> categories(@RequestParam String from, @RequestParam String to) {

    Date dateFrom = Date.from(Instant.ofEpochMilli(Long.valueOf(from)));
    Date dateTo = Date.from(Instant.ofEpochMilli(Long.valueOf(to)));

    ArrayList<CategoriesResponse> categoriesResponses = new ArrayList<>();
    List<Product> productList = repository.findByExpirationDate(dateFrom, dateTo);
    Map<String, Long> categories = new HashMap<>();
    for(Product product : productList){
      if(categories.get(product.getCategory()) == null){
        categories.put(product.getCategory(), 1L);
      }else{
        Long count = categories.get(product.getCategory());
        categories.put(product.getCategory(), count + 1);
      }
    }

    Iterator<Map.Entry<String, Long>> iterator = categories.entrySet().iterator();
    Long index = 0L;
    while(iterator.hasNext()){
      Map.Entry<String, Long> entry = iterator.next();
      categoriesResponses.add(new CategoriesResponse(index++, entry.getKey(), entry.getValue()));
    }

    return categoriesResponses;
  }


}

class CategoriesResponse {
  String name;
  Long count;
  Long id;

  public CategoriesResponse(Long id, String name, Long count) {
    this.name = name;
    this.count = count;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public Long getCount() {
    return count;
  }

  public void setCount(Long count) {
    this.count = count;
  }

  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }
}
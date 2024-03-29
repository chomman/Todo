/* 
* Generated by
* 
*      _____ _          __  __      _     _
*     / ____| |        / _|/ _|    | |   | |
*    | (___ | | ____ _| |_| |_ ___ | | __| | ___ _ __
*     \___ \| |/ / _` |  _|  _/ _ \| |/ _` |/ _ \ '__|
*     ____) |   < (_| | | | || (_) | | (_| |  __/ |
*    |_____/|_|\_\__,_|_| |_| \___/|_|\__,_|\___|_|
*
* The code generator that works in many programming languages
*
*			https://www.skaffolder.com
*
*
* You can generate the code from the command-line
*       https://npmjs.com/package/skaffolder-cli
*
*       npm install -g skaffodler-cli
*
*   *   *   *   *   *   *   *   *   *   *   *   *   *   *   *
*
* To remove this comment please upgrade your plan here: 
*      https://app.skaffolder.com/#!/upgrade
*
* Or get up to 70% discount sharing your unique link:
*       https://app.skaffolder.com/#!/register?friend=5dcb97a2f1ef4518a5382d3c
*
* You will get 10% discount for each one of your friends
* 
*/
package com.todo.controller.base;

import java.util.List;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import java.util.ArrayList;
import org.springframework.security.access.annotation.Secured;
import org.springframework.beans.factory.annotation.Autowired;
import com.todo.db.todo_db.service.TodoService;
import com.todo.db.todo_db.entity.Todo;

//IMPORT RELATIONS
import com.todo.db.todo_db.entity.Category;
import com.todo.db.todo_db.entity.Tag;

public class TodoBaseController {
    
    @Autowired
	TodoService todoService;



//CRUD METHODS


    //CRUD - CREATE
    @Secured({ "ROLE_PRIVATE_USER" })
		@RequestMapping(value = "/todo", method = RequestMethod.POST, headers = "Accept=application/json")
	public Todo insert(@RequestBody Todo obj) {
		Todo result = todoService.insert(obj);

	    
		//external relation category
		ArrayList<Long> category = obj.getCategory();
		if (category != null) {
			TodoService.Todo_categoryService.updateRelation(result.get_id(), category);
		}
		
		//external relation tags
		ArrayList<Long> tags = obj.getTags();
		if (tags != null) {
			TodoService.Todo_tagsService.updateRelation(result.get_id(), tags);
		}
		
		
		return result;
	}

	
    //CRUD - REMOVE
    @Secured({ "ROLE_PRIVATE_USER" })
	@RequestMapping(value = "/todo/{id}", method = RequestMethod.DELETE, headers = "Accept=application/json")
	public void delete(@PathVariable("id") Long id) {
		todoService.delete(id);
	}
	

    //CRUD - FIND BY Category
    @Secured({ "ROLE_PRIVATE_USER" })
	@RequestMapping(value = "/todo/findBycategory/{key}", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<Todo> findBycategory(@PathVariable("key") Long idcategory) {
		List<Todo> list = todoService.findBycategory(idcategory);
		return list;
	}

    //CRUD - FIND BY Tags
    @Secured({ "ROLE_PRIVATE_USER" })
	@RequestMapping(value = "/todo/findBytags/{key}", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<Todo> findBytags(@PathVariable("key") Long idtags) {
		List<Todo> list = todoService.findBytags(idtags);
		return list;
	}
	
    //CRUD - GET ONE
    @Secured({ "ROLE_PRIVATE_USER" })
	@RequestMapping(value = "/todo/{id}", method = RequestMethod.GET, headers = "Accept=application/json")
	public Todo get(@PathVariable Long id) {
		Todo obj = todoService.get(id);
		
		
		//external relation category
		ArrayList<Long> category = TodoService.Todo_categoryService.findBy_Todo(id);
		obj.setCategory(category);
		
		//external relation tags
		ArrayList<Long> tags = TodoService.Todo_tagsService.findBy_Todo(id);
		obj.setTags(tags);
		
		
		return obj;
	}
	
	
    //CRUD - GET LIST
    @Secured({ "ROLE_PRIVATE_USER" })
	@RequestMapping(value = "/todo", method = RequestMethod.GET, headers = "Accept=application/json")
	public List<Todo> getList() {
		return todoService.getList();
	}
	
	

    //CRUD - EDIT
    @Secured({ "ROLE_PRIVATE_USER" })
	@RequestMapping(value = "/todo/{id}", method = RequestMethod.POST, headers = "Accept=application/json")
	public Todo update(@RequestBody Todo obj, @PathVariable("id") Long id) {
		Todo result = todoService.update(obj, id);

	    
		//external relation category
		ArrayList<Long> category = obj.getCategory();
		if (category != null) {
			TodoService.Todo_categoryService.updateRelation(id, category);
		}
		
		//external relation tags
		ArrayList<Long> tags = obj.getTags();
		if (tags != null) {
			TodoService.Todo_tagsService.updateRelation(id, tags);
		}
		
		
		return result;
	}
	


/*
 * CUSTOM SERVICES
 * 
 *	These services will be overwritten and implemented in  Custom.js
 */


	
}

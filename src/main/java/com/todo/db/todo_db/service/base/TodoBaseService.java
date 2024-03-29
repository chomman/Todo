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
package com.todo.db.todo_db.service.base;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;
import org.springframework.stereotype.Service;

import com.todo.db.todo_db.entity.Todo;
import com.todo.db.todo_db.service.TodoService;

//IMPORT RELATIONS
import com.todo.db.todo_db.entity.Category;
import com.todo.db.todo_db.entity.Tag;

@Service
public class TodoBaseService {

	private static NamedParameterJdbcTemplate jdbcTemplate;
	
		@Autowired
	public void setDataSource(DataSource dataSource) {
		jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
	}
	


    //CRUD METHODS
    
    //CRUD - CREATE
    	
	public Todo insert(Todo obj) {
		Long id = jdbcTemplate.queryForObject("select max(_id) from `todo`", new MapSqlParameterSource(), Long.class);
		obj.set_id(id == null ? 1 : id + 1);
		String sql = "INSERT INTO `todo` (`_id`, `details`,`status`,`title`) VALUES (:id,:details,:status,:title)";
			SqlParameterSource parameters = new MapSqlParameterSource()
		    .addValue("id", obj.get_id())
			.addValue("details", obj.getDetails())
			.addValue("status", obj.getStatus())
			.addValue("title", obj.getTitle());
		
		jdbcTemplate.update(sql, parameters);
		return obj;
	}
	
    	
    //CRUD - REMOVE
    
	public void delete(Long id) {
		String sql = "DELETE FROM `Todo` WHERE `_id`=:id";
		SqlParameterSource parameters = new MapSqlParameterSource()
			.addValue("id", id);
		
		String sql_category = "DELETE FROM `todo_category` WHERE `id_Todo`= :id";
		jdbcTemplate.update(sql_category, parameters);
		String sql_tags = "DELETE FROM `todo_tags` WHERE `id_Todo`= :id";
		jdbcTemplate.update(sql_tags, parameters);
		jdbcTemplate.update(sql, parameters);
	}

    	
    //CRUD - FIND BY Category
    	
	public List<Todo> findBycategory(Long idcategory) {
		
        String sql = "select * from `Todo` WHERE `_id` IN (SELECT `id_Todo` FROM `Todo_category` WHERE `id_Category` = :idcategory)";
		
	    SqlParameterSource parameters = new MapSqlParameterSource()
		.addValue("idcategory", idcategory);
	    
	    return jdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper(Todo.class));
	}
    	
    //CRUD - FIND BY Tags
    	
	public List<Todo> findBytags(Long idtags) {
		
        String sql = "select * from `Todo` WHERE `_id` IN (SELECT `id_Todo` FROM `Todo_tags` WHERE `id_Tag` = :idtags)";
		
	    SqlParameterSource parameters = new MapSqlParameterSource()
		.addValue("idtags", idtags);
	    
	    return jdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper(Todo.class));
	}
    	
    //CRUD - GET ONE
    	
	public Todo get(Long id) {
	    
		String sql = "select * from `Todo` where `_id` = :id";
		
	    SqlParameterSource parameters = new MapSqlParameterSource()
			.addValue("id", id);
	    
	    return (Todo) jdbcTemplate.queryForObject(sql, parameters, new BeanPropertyRowMapper(Todo.class));
	}


    	
        	
    //CRUD - GET LIST
    	
	public List<Todo> getList() {
	    
		String sql = "select * from `Todo`";
		
	    SqlParameterSource parameters = new MapSqlParameterSource();
	    return jdbcTemplate.query(sql, parameters, new BeanPropertyRowMapper(Todo.class));
	    
	    
	}

    	
    //CRUD - EDIT
    	
	public Todo update(Todo obj, Long id) {

		String sql = "UPDATE `Todo` SET `details` = :details,`status` = :status,`title` = :title  WHERE `_id`=:id";
		SqlParameterSource parameters = new MapSqlParameterSource()
			.addValue("id", id)
			.addValue("details", obj.getDetails())
			.addValue("status", obj.getStatus())
			.addValue("title", obj.getTitle());
		jdbcTemplate.update(sql, parameters);
	    return obj;
	}
	
    	
    
    
    
    
    // External relation m:m category
    public static class Todo_categoryService {

    	public static ArrayList<Long> findBy_Todo(Long id_Todo) {
    		String sql = "select `id_Category` from Todo_category WHERE `id_Todo`=:id_Todo";
    		SqlParameterSource parameters = new MapSqlParameterSource()
    			.addValue("id_Todo", id_Todo);
    	    
    	    List<Long> listId = jdbcTemplate.queryForList(sql, parameters, Long.class);
    		return (ArrayList<Long>) listId;
    	}
    	

    	public static void updateRelation(Long id_Todo, ArrayList<Long> category) {

    		// Delete not in array
    		String in = " and `id_Category` NOT IN (:category)";
    		String sql = "DELETE FROM Todo_category WHERE `id_Todo`=:id_Todo ";
    		
    		if (category != null && category.size() > 0)
    			sql = sql + in;
    			
    		SqlParameterSource parameters = new MapSqlParameterSource()
    			.addValue("id_Todo", id_Todo)
    			.addValue("category", category);
    		
    		jdbcTemplate.update(sql, parameters);
    		
    		// Get actual    		
    	    List<Long> actual = TodoService.Todo_categoryService.findBy_Todo(id_Todo);
    	    
    		// Insert new
    		for (Long id_Category : category) {
    			if (actual.indexOf(id_Category) == -1){
    				TodoService.Todo_categoryService.insert(id_Todo, id_Category);
    			}
    		}
    		
    	}
    	

    	public static void insert(Long id_Todo, Long id_Category) {
    		Todo.Todo_category obj = new Todo.Todo_category();
			Long id = jdbcTemplate.queryForObject("select max(_id) FROM Todo_category", new MapSqlParameterSource(), Long.class);
			obj.set_id(id == null ? 1 : id + 1);
			
			String sql = "INSERT INTO Todo_category (`_id`, `id_Todo`, `id_Category` )	VALUES (:id, :id_Todo, :id_Category)";

			MapSqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("id", obj.get_id())
				.addValue("id_Todo", id_Todo)
				.addValue("id_Category", id_Category);

			jdbcTemplate.update(sql, parameters);
    	}

    }
	    
    
    // External relation m:m tags
    public static class Todo_tagsService {

    	public static ArrayList<Long> findBy_Todo(Long id_Todo) {
    		String sql = "select `id_Tag` from Todo_tags WHERE `id_Todo`=:id_Todo";
    		SqlParameterSource parameters = new MapSqlParameterSource()
    			.addValue("id_Todo", id_Todo);
    	    
    	    List<Long> listId = jdbcTemplate.queryForList(sql, parameters, Long.class);
    		return (ArrayList<Long>) listId;
    	}
    	

    	public static void updateRelation(Long id_Todo, ArrayList<Long> tags) {

    		// Delete not in array
    		String in = " and `id_Tag` NOT IN (:tags)";
    		String sql = "DELETE FROM Todo_tags WHERE `id_Todo`=:id_Todo ";
    		
    		if (tags != null && tags.size() > 0)
    			sql = sql + in;
    			
    		SqlParameterSource parameters = new MapSqlParameterSource()
    			.addValue("id_Todo", id_Todo)
    			.addValue("tags", tags);
    		
    		jdbcTemplate.update(sql, parameters);
    		
    		// Get actual    		
    	    List<Long> actual = TodoService.Todo_tagsService.findBy_Todo(id_Todo);
    	    
    		// Insert new
    		for (Long id_Tag : tags) {
    			if (actual.indexOf(id_Tag) == -1){
    				TodoService.Todo_tagsService.insert(id_Todo, id_Tag);
    			}
    		}
    		
    	}
    	

    	public static void insert(Long id_Todo, Long id_Tag) {
    		Todo.Todo_tags obj = new Todo.Todo_tags();
			Long id = jdbcTemplate.queryForObject("select max(_id) FROM Todo_tags", new MapSqlParameterSource(), Long.class);
			obj.set_id(id == null ? 1 : id + 1);
			
			String sql = "INSERT INTO Todo_tags (`_id`, `id_Todo`, `id_Tag` )	VALUES (:id, :id_Todo, :id_Tag)";

			MapSqlParameterSource parameters = new MapSqlParameterSource()
				.addValue("id", obj.get_id())
				.addValue("id_Todo", id_Todo)
				.addValue("id_Tag", id_Tag);

			jdbcTemplate.update(sql, parameters);
    	}

    }
	

    
    /*
     * CUSTOM SERVICES
     * 
     *	These services will be overwritten and implemented in TodoService.java
     */
    

}

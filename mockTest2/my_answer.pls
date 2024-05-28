-- Use the following relational schema to answer the next questions.
--   Recipe(name, prepTime, cookTime, servingsNumber, cuisineType)
--   Instruction(step, recipeName, text)
--   Cuisine(cuisineType, description)
--   Ingredient(ingredientName, containsDairy, containsGluten)
--   Use(recipeName, ingredientName, quantity)

-- (a) [2 marks] Write an SQL query that finds the French recipes.
SELECT * FROM Recipe WHERE cuisineType = 'French';

-- (b) [2 marks] Write an SQL query that finds the names of all recipes that use eggs.
SELECT recipeName FROM Use WHERE ingredientName = 'eggs'

-- (c) [3 marks] Write an SQL query that shows how many instruction steps it takes to create each recipe, ordered from longest recipe to shortest recipe.
SELECT recipeName, count(step) FROM Instruction GROUP BY recipeName ORDER BY count(step);

-- (d) [3 marks] Write an SQL query that finds the names of every recipe that uses only gluten free ingredients.
SELECT r.name FROM Recipe r WHERE NOT EXISTS (SELECT * FROM Ingredient i WHERE i.recipeName = r.name AND containsGluten = 1);

-- (e) [4 marks] Write an SQL query that finds the names of all recipes that use more than 3 types of ingredients.
SELECT recipename FROM Use GROUP BY recipename HAVING count(ingredientName) > 3;

-- (f) [4 marks] Write an SQL query that shows all the instructions of recipes that use fewer than 3 ingredients.
SELECT i.* FROM Instruction i 
  WHERE EXISTS (
    SELECT * FROM Use u
      WHERE u.recipeName = i.recipeName
      GROUP BY u.recipeName
      HAVING 3 > count(u.ingredientName)
  );

-- (g) [5 marks] Write an SQL query that finds the ingredients that are used by recipes of every cuisine type.
SELECT i.* FROM Ingredient i WHERE (SELECT count(DISTINCT c.cuisineType) FROM Cuisine c) = (SELECT count(DISTINCT r.cuisineType) FROM Use u JOIN Recipe r ON r.name = u.recipeName WHERE u.ingredientName = i.ingredientName);

SELECT i.* FROM Ingredient i WHERE NOT EXISTS (SELECT * FROM Cuisine c WHERE NOT EXISTS (select * from recipe r join use u where u.recipename = r.recipename and  i.cuisineType = c.cuisineType and u.ingredientName = i.name))



-- Indexing
-- (a) [2 marks] Create an index that would speed up the query that finds all recipes that take less than 10 minutes to cook.
CREATE INDEX recipes_cookTime ON Recipe(cookTime);

-- (b) [2 marks] Consider the query “select * from recipe where cuisineType= ‘NZ’”. What sort of index would be helpful here and how would you implement it in Oracle?
-- bitmap
CREATE BITMAP INDEX recipe_cuisine_type_bitmap ON recipe(cuisineType);
-- CREATE CLUSTER recipe_cuisine_type_cluster (cuisineType varchar(10)) size 32 single table hashkeys 1000 hash is id;

-- (c) [2 marks] Give an example of when you would create a B*-Tree index, and briefly explain why.
-- for searching a range of data from large dataset 
-- because it chunks data to pieces segments. so it can search range data fast by traversal specific keys

-- (d) [2 marks] Give an example of when you would create a bitmap index, and briefly explain why.


-- (e) [2 marks] What are two disadvantages of creating indexes?
-- It will be one more step in each query to search index table. It wouldn't help with low data amount.
-- It will be a little bit slow when updating and inserting rows, because it has to update index table at same time.
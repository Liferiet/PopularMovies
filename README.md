# Popular Movies Project

## Project Overview
In this project you can find what movies are currently popular and top rated according the MovieDB. Read the details, watch trailers and view reviews about the selected movie. You can save and scroll through your favorite movies.

## What in this Project

Building a layout and populating its fields from data received as JSON. Using RecyclerView with Grid Layout Manager. Store favourite movies in persistence Room Database.

## What to do to run this project?
Obtain API key from https://www.themoviedb.org/. Generate file "secrets.xml" in res/values with given code:

```
<?xml version="1.0" encoding="utf-8"?>
<resources>
    <string name="API_KEY"></string>
</resources>
```

And paste obtained API key between ```<string></string>``` tags.

## More TODO in this application
- Add Paging to retrieve more than one page from database (only 20 movies)
- Possibly add more external data sources about movies (unfortunately, most of them are paid)
- Change title which is displayed in MainActivity to current selected option in the drawer
- Change the way the reviews are displayed
- Extract API_KEY to external file as a secret
- Add internationalization support(?) (MovieDB is in english)
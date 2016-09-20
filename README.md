# Android-OkHttp3PostMultiPart

Server

```java
<?php

    $file_path = "/home/a5754819/public_html/retrofit/uploads/";

    $file = $file_path.'post.txt';
    $contents = file_get_contents($file);
    $contents .= $_POST["name"]." ";
    $contents .= $_POST["id"]." ";
    file_put_contents($file, $contents, FILE_APPEND);   

    $file_path = $file_path . basename( $_FILES['uploaded_image']['name']);
    if(move_uploaded_file($_FILES['uploaded_image']['tmp_name'], $file_path)) {
        echo "success";
    } else{
        echo "fail";
    }

?>
```

If you are only posting form fields, do this:

```java
new FormBody.Builder()
                .add("key1", "field1")
                .add("key2", "field2")
                .build();
```

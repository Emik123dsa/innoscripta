## The main idea of this solution:

- Use a simple shell scripts:

**Idea:**

We can create simple BASH script, which will be gathering all of the files and gzipping them into one file:

First of all, we need to scan the directory with the files:

```sh
ls -la | awk '{print $9}' | xargs -n2 sh -i -c 'convert "$1" -quality <convert_percentage> <output_dir>/convert."$1"'
```

Or via wildcard:

```sh
wildcard <src_project>/*.<mime_type> | xargs -n2 sh -i -c 'convert "$1" -quality <convert_percentage> <output_dir>/convert."$1"'
```

After we are required to compress it as much as we can:

```sh
tar -jcvf <file_directory>.tar bz2 <project_directory>
```

Or compress it only into one single file:

```sh
gzip <file_directory>.gz 
```

Afterwards, we can locally download all of the required files with SCP (if ssh is allowed it):

```sh
scp <username>@<hostname>:<stdin_directory> <stdout_directory>
```

- Implement own directory parser, converter and dispatcher with Java:

**Idea:**

We have to follow next steps:

- Find all images in the directory and subdirectories with required MIME/type;

- Convert and compress them into small images;

- Dispatch to the FTP remote server or straightforwardly download into your system;

This method will also minimize time of images downloading
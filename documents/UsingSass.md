###Installation
- Install Ruby
- Install Sass
- Install the "File Watchers" plugin from File->Settings->Plugins->Marketplace
- Restart IntelliJ
- Go to settings ->Tools->File Watchers.
- Press the plus sign, and press SCSS
- Change the "arguments" line to:
$FileName$:../css/$FileNameWithoutExtension$.css

###Use
```
cd src/main/webapp/
sass --watch sass/desktop.sass:css/desktop.css

- Press OK
- Repeat the previous 3 steps, but then for Sass
- Press apply
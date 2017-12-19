const fs = require('fs');

var file = fs.readFileSync(process.argv[2], "UTF-8");

var lines = file.split('\n');

console.log("{");
var padding="    "; 
for (var i = 0; i < lines.length; ++i) {
    var line = lines[i];
    line = line.trim();

    if (line.length == 0)
        continue;
    
    if (i !== (lines.length - 2))
        console.log(padding + line + ",");
    else
        console.log(padding + line);

}
console.log("}");
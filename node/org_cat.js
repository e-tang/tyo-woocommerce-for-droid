const fs = require('fs');

var file = fs.readFileSync(process.argv[2], "UTF-8");

var lines = file.split('\n');

console.log("[");
var padding="    ";
var cats = new Set();
var count = 0, subcount = 0;

for (var i = 0; i < lines.length; ++i) {
    var line = lines[i];
    line = line.trim();

    if (line.length == 0)
        continue;
    
    var cols = line.split('\t');
    var buffer = "";
    if (cols.length > 1) {
        var t1 = cols[0].split(" ");
        var t2 = cols[1].split(" ");
        var cat = ""
        var j = 0;
        for (j = 0; j < t1.length; ++j) {
            if (!t1[j] || t1[j].length == 0)
                continue;

            var str = t1[j][0] + t1[j].toLowerCase().substr(1);
            if (j == 0)
                buffer = str;
            else
                buffer += " " + str;
        }
        buffer = buffer.trim();
        if (!cats.has(buffer)) {
            // if (subcount > 0)
            cats.add(buffer);
            subcount = 0;
            if (count > 0)
                console.log(',');
            console.log('"' + buffer + '"');
            //     console.log('],');
            // console.log('"' + buffer + '": [');
            ++count;
        }

        console.log(',');
            
        buffer = "";
        for (j = 1; j < t2.length; ++j) {
            if (t2[j].length == 0)
                continue;
            var str = t2[j][0] + t2[j].toLowerCase().substr(1);
            if (j == 1)
                buffer = str;
            else
                buffer += " " + str;
        }
        console.log('"' + buffer.trim() + '"');
        ++subcount;
        // console.log(buffer);
    }
}
// console.log("]");
console.log("]");
cd /JSites/templates/unitsnew/xslt

/usr/bin/wget -o log /var/log/fetch_units.log https://www.units.it/grafica4out/ute/xhtml_body/

cat begin_container.xslt index.html end_container.xslt > container.xslt
rm index.html

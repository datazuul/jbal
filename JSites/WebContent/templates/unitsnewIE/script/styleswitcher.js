// la fonte principale di ispirazione e'
// http://www.alistapart.com/articles/alternate/
// pero' poi modificate in diversi punti (ma quel doc puo' servire
// da spiegazione generale).

function setActiveStyleSheet(title) {
  var i, a, patdom;
  patdom = "; path=/; domain=.units.it";
  // 'disattivo' il cookie comunque (anche se non esiste):
  // (il valore non importa, conta la data nel passato)
  document.cookie = "unitsfontsize="+
                    "; expires=Thu, 01-Jan-1970 00:00:01 GMT"+
                    patdom;
  for(i=0; (a = document.getElementsByTagName("link")[i]); i++) {
    if(a.getAttribute("rel").indexOf("style") != -1
       && a.getAttribute("title")) {
           a.disabled = true;
           if(a.getAttribute("title") == title) {
               a.disabled = false;
               // attivo il cookie (senza expiration, quindi
               // expiration alla chiusura del browser):
               document.cookie = "unitsfontsize="+title+patdom;
           }
    }
  }
}

function readCookie(name) {
  var i, ca, c, val, nameEQ = name + "=";
  ca = document.cookie.split(';');
  for(i=0; i < ca.length; i++) {
    c = ca[i].replace(/^\s+|\s+$/g, ''); // strip degli spazi
    if (c.indexOf(nameEQ) == 0) {
        val = c.substring(nameEQ.length,c.length);
        if (val && val != "null") return val;
    }
  }
  return null;
}

var cookieval = readCookie("unitsfontsize");
if (cookieval) setActiveStyleSheet(cookieval);
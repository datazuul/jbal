var objPopUpWindow;

function isAutenticato()
{
	
	
}

function openPopup(passo,idCodiceDest,idDescrizioneDest,inputCode,numeroCaratteri,inputCode2)
	{
		if (inputCode.length < numeroCaratteri)
                {
			alert("Inserire almeno "+numeroCaratteri+" caratteri per la decodifica!");
			return false;
		}
		else
		{	
                    var stile = "top=10, left=10, width=600, height=400, toolbar= 0,location= 0,directories= 0,status= 0,menubar= 0,scrollbars= 1,resizable= 0,copyhistory= 0";
                    var objPopUpWindow = window.open(contextpath+"/RichiestaServiziBase?passo="+passo+"&idCodiceDest="+idCodiceDest+"&idDescrizioneDest="+idDescrizioneDest+"&inputCode="+inputCode+"&inputCode2="+inputCode2,"POPUP", stile);
		}
	}	

// Funzione che intercetta il tasto premuto e verifica che sia: 
// un numero
// la virgola
// il segno meno
// il punto

function accettaNumeri(e,accettavirgola,accettapunto,accettameno,campoGestito)
   {
        var valoreCampo = "";
        if (campoGestito)
        {
            valoreCampo = campoGestito.value+"";
        }
	key = getkey(e);
	// alert(key);
	if (key == 0) return true;                            // caratteri speciali (tab,frecce,delete)	
        if (key == 8) return true;                            // caratteri speciali (back space)	
        // NUMERI
        if (key > 47 && key < 58) return true;
	// PUNTO
        if (accettapunto == 'S' && key == 46) return true;
	// MENO
        if (accettameno == 'S' && key == 45 && valoreCampo.indexOf("-") == -1) 
        {
            if (campoGestito)
            {
                campoGestito.value = "-" + valoreCampo;
                return false;
            }
            else
            {
                return true;
            }
        }
        // VIRGOLA
	if (accettavirgola == 'S' && key == 44 && valoreCampo.indexOf(",") == -1) return true; // virgola
	
	return false;
   }
   
function getkey(e)
{
    if (window.event)
       return window.event.keyCode;
    else if (e)
       return e.which;
    else
       return null;
} 
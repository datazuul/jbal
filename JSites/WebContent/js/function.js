function catcalc(cal) {
			        var date = cal.date;
			        var time = date.getTime();
			        var modifico=cal.params.inputField;
			        
			        // use the _other_ field
			        var field = document.getElementById("startdate");
			        if (field == cal.params.inputField) {
			            field = document.getElementById("enddate");
			        }

			        // se è vuoto metti la data uguale
			        if(Number(field.value)==0) {
			            	field.value=date.print("%Y%m%d");
			        }
			        
			        var data=new Date();
			        data.setFullYear(Number(field.value.substr(0,4))); // substr inizia a contare da 0
			        data.setMonth(Number(field.value.substr(4,2))-1);  // i mesi vanno da 0 a 11
			        data.setDate(Number(field.value.substr(6,2)));
			        
			        if((modifico==document.getElementById("startdate") && time > data.getTime()) || 
			        		(modifico==document.getElementById("enddate") && data.getTime() > time)) {
			        	alert("La data di inizio deve essere antecedente o uguale alla data di fine avviso!");
			        	field.value=date.print("%Y%m%d");
			        }
			        
			        if(Math.abs(time-data.getTime())>2592000000) {
			        	alert("Un avviso no può essere valido per piu' di 30 giorni!")
			        	field.value=date.print("%Y%m%d");
			        }
			        
			        
			    }
			    Calendar.setup({
			        inputField     :    "startdate",     // id of the input field
			        ifFormat       :    "%Y%m%d",      // format of the input field
			        button         :    "f_trigger_c",  // trigger for the calendar (button ID)
			        align          :    "Br",           // alignment (defaults to "Bl")
			        singleClick    :    true,
			        onUpdate       :    catcalc
			    });
			    Calendar.setup({
			        inputField     :    "enddate",     // id of the input field
			        ifFormat       :    "%Y%m%d",      // format of the input field
			        button         :    "f_trigger_d",  // trigger for the calendar (button ID)
			        align          :    "Br",           // alignment (defaults to "Bl")
			        singleClick    :    true,
			        onUpdate       :    catcalc
			    });
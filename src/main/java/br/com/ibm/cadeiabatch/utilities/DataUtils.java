package br.com.ibm.cadeiabatch.utilities;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public class DataUtils{

    private final int[] FIXED_HOLIDAY_DAY = {1,21,1,9,7,12,2,15,25};
    private final int[] FIXED_HOLIDAY_MONTH = {0,3,4,6,8,9,10,10,11};
    private static final int[] FIXED_HOLIDAY_DAY_STATIC = {1,21,1,9,7,12,2,15,25};
    private static final int[] FIXED_HOLIDAY_MONTH_STATIC = {0,3,4,6,8,9,10,10,11};
    
    public Calendar getFirstDayLastWeek() {
    	
    	Calendar calendar = Calendar.getInstance();
    	calendar.setFirstDayOfWeek(Calendar.SATURDAY);
    	calendar.add(Calendar.WEEK_OF_YEAR, -3);
    	int diaSemana = calendar.get(Calendar.DAY_OF_WEEK);
    	calendar.add(Calendar.DAY_OF_MONTH, Calendar.SATURDAY - diaSemana);
    	
    	return calendar;
    }
    
    public List<Calendar> getAllDaysFrom(Calendar calFrom) {
    	
    	Calendar today = Calendar.getInstance();
    	List<Calendar> dates = new ArrayList<>();
    	
    	while(calFrom.compareTo(today) <= 0) {
    		Calendar newCal = Calendar.getInstance();
    		newCal.setTime(calFrom.getTime());
    		dates.add(newCal);
    		calFrom.add(Calendar.DATE, +1);
    	}
    	
    	return dates;
    }
    
    public Calendar getFirstDayCurrentWeek() {
		Calendar sabado = Calendar.getInstance();
		sabado.add(Calendar.WEEK_OF_YEAR, -1);
    	sabado.setFirstDayOfWeek(Calendar.SATURDAY);
    	int diaSemana = sabado.get(Calendar.DAY_OF_WEEK);
    	sabado.add(Calendar.DAY_OF_MONTH, Calendar.SATURDAY - diaSemana);
    	
    	return sabado;
    }
    
    public Calendar getLastDayCurrentWeek() {
		Calendar sexta = Calendar.getInstance();
		sexta.setFirstDayOfWeek(Calendar.SATURDAY);
    	int diaSemana = sexta.get(Calendar.DAY_OF_WEEK);
    	sexta.add(Calendar.DAY_OF_MONTH, Calendar.FRIDAY - diaSemana);
    	
    	return sexta;
    }
    
    public List<Calendar> getDaysOfCurrentWeek() {
    	List<Calendar> diasSemana = new ArrayList<>();
    	
    	Calendar sabado = Calendar.getInstance();
    	sabado.add(Calendar.WEEK_OF_YEAR, -1);
    	sabado.setFirstDayOfWeek(Calendar.SATURDAY);
    	int diaSemana = sabado.get(Calendar.DAY_OF_WEEK);
    	sabado.add(Calendar.DAY_OF_MONTH, Calendar.SATURDAY - diaSemana);
    	
    	Calendar domingo = Calendar.getInstance();
    	domingo.setFirstDayOfWeek(Calendar.SATURDAY);
    	domingo.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - diaSemana);
    	
    	Calendar segunda = Calendar.getInstance();
    	segunda.setFirstDayOfWeek(Calendar.SATURDAY);
    	segunda.add(Calendar.DAY_OF_MONTH, Calendar.MONDAY - diaSemana);
    	
    	Calendar terca = Calendar.getInstance();
    	terca.setFirstDayOfWeek(Calendar.SATURDAY);
    	terca.add(Calendar.DAY_OF_MONTH, Calendar.TUESDAY - diaSemana);
    	
    	Calendar quarta = Calendar.getInstance();
    	quarta.setFirstDayOfWeek(Calendar.SATURDAY);
    	quarta.add(Calendar.DAY_OF_MONTH, Calendar.WEDNESDAY - diaSemana);
    	
    	Calendar quinta = Calendar.getInstance();
    	quinta.setFirstDayOfWeek(Calendar.SATURDAY);
    	quinta.add(Calendar.DAY_OF_MONTH, Calendar.THURSDAY - diaSemana);
    	
    	Calendar sexta = Calendar.getInstance();
    	sexta.setFirstDayOfWeek(Calendar.SATURDAY);
    	sexta.add(Calendar.DAY_OF_MONTH, Calendar.FRIDAY - diaSemana);
    	
    	
    	diasSemana.add(sabado);
    	diasSemana.add(domingo);
    	diasSemana.add(segunda);
    	diasSemana.add(terca);
    	diasSemana.add(quarta);
    	diasSemana.add(quinta);
    	diasSemana.add(sexta);
    	
    	return diasSemana;
    }
    
    //TESTE
    public List<Calendar> getDaysOfLastWeek() {
    	List<Calendar> diasSemanaPassada = new ArrayList<>();
    	
    	Calendar sabado = Calendar.getInstance();
    	sabado.add(Calendar.WEEK_OF_YEAR, -1);
    	sabado.setFirstDayOfWeek(Calendar.SATURDAY);
    	int diaSemana = sabado.get(Calendar.DAY_OF_WEEK);
    	diaSemana = diaSemana + 7;
    	sabado.add(Calendar.DAY_OF_MONTH, Calendar.SATURDAY - diaSemana);
    	
    	Calendar domingo = Calendar.getInstance();
    	domingo.setFirstDayOfWeek(Calendar.SATURDAY);
    	domingo.add(Calendar.DAY_OF_MONTH, Calendar.SUNDAY - diaSemana);
    	
    	Calendar segunda = Calendar.getInstance();
    	segunda.setFirstDayOfWeek(Calendar.SATURDAY);
    	segunda.add(Calendar.DAY_OF_MONTH, Calendar.MONDAY - diaSemana);
    	
    	Calendar terca = Calendar.getInstance();
    	terca.setFirstDayOfWeek(Calendar.SATURDAY);
    	terca.add(Calendar.DAY_OF_MONTH, Calendar.TUESDAY - diaSemana);
    	
    	Calendar quarta = Calendar.getInstance();
    	quarta.setFirstDayOfWeek(Calendar.SATURDAY);
    	quarta.add(Calendar.DAY_OF_MONTH, Calendar.WEDNESDAY - diaSemana);
    	
    	Calendar quinta = Calendar.getInstance();
    	quinta.setFirstDayOfWeek(Calendar.SATURDAY);
    	quinta.add(Calendar.DAY_OF_MONTH, Calendar.THURSDAY - diaSemana);
    	
    	Calendar sexta = Calendar.getInstance();
    	sexta.setFirstDayOfWeek(Calendar.SATURDAY);
    	sexta.add(Calendar.DAY_OF_MONTH, Calendar.FRIDAY - diaSemana);
    	
    	diasSemanaPassada.add(sabado);
    	diasSemanaPassada.add(domingo);
    	diasSemanaPassada.add(segunda);
    	diasSemanaPassada.add(terca);
    	diasSemanaPassada.add(quarta);
    	diasSemanaPassada.add(quinta);
    	diasSemanaPassada.add(sexta);
    	
    	return diasSemanaPassada;
    }
    
    public Calendar getInicialDateDaily(Calendar procDate){
        Calendar startDate = null;
        Calendar yesterday = (Calendar) procDate.clone();
        yesterday.add(Calendar.DAY_OF_MONTH, -1);
        if (isWorkDay(yesterday)){
            startDate = yesterday;
        } else {
            startDate = GetLastWorkDay(yesterday);
        }
        startDate.set(Calendar.HOUR, 18);
        return startDate;
    }
    public Calendar getInicialDateMonthly(Calendar procDate){
        Calendar startDate = (Calendar) procDate.clone();
        startDate.set(Calendar.DAY_OF_MONTH,1);
        startDate.set(Calendar.HOUR, 0);
        return startDate;
    }

    public Boolean isWorkDay(Calendar date){
        if(date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            return false;
        }else if (IsFixedHoliday(date)){
            return false;
        } else if (IsCalculatedHoliday(date)){
            return false;
        }
        return true;
    }
    public static Boolean isWorkDayStatic(Calendar date){
        if(date.get(Calendar.DAY_OF_WEEK) == Calendar.SUNDAY || date.get(Calendar.DAY_OF_WEEK) == Calendar.SATURDAY){
            return false;
        }else if (IsFixedHolidayStatic(date)){
            return false;
        } else if (IsCalculatedHolidayStatic(date)){
            return false;
        }
        return true;
    }
    
    public Boolean IsFixedHoliday(Calendar date){
        for (int i = 0; i < FIXED_HOLIDAY_DAY.length; i++){
            if (date.get(Calendar.DAY_OF_MONTH) == FIXED_HOLIDAY_DAY[i] && date.get(Calendar.MONTH) == FIXED_HOLIDAY_MONTH[i]){
                return true;
            }
        }
        return false;
    }
    public static Boolean IsFixedHolidayStatic(Calendar date){
        for (int i = 0; i < FIXED_HOLIDAY_DAY_STATIC.length; i++){
            if (date.get(Calendar.DAY_OF_MONTH) == FIXED_HOLIDAY_DAY_STATIC[i] && date.get(Calendar.MONTH) == FIXED_HOLIDAY_MONTH_STATIC[i]){
                return true;
            }
        }
        return false;
    }
    public Boolean IsCalculatedHoliday(Calendar date){
        
		// Feriados móveis
		// 9: Segunda Carnaval (48 dias antes da Páscoa)
		// 10: Terca Carnaval (1 dia apos segunda de carnaval)
		// 11: Sexta Feira Santa (02 dias antes da Páscoa)
		// 12: Corpus Christi (60 dias apos a Páscoa)

        // A data da pácoa é data base para cálculo dos outros feriados móveis.
        
        int year = date.get(Calendar.YEAR);

		// Algoritmo de Gauss
		double c, n, m, k, i, j, l, d;

		c = year / 100;
		c = year / 100;
		n = year - Math.floor(19 * Math.floor(year / 19));
		k = Math.floor((c - 17) / 25);
		i = Math.floor(c) - Math.floor(c / 4) - Math.floor((c - k) / 3)
				+ Math.floor(19 * n) + 15;
		i = Math.floor(i) - Math.floor(30 * Math.floor(i / 30));
		i = Math.floor(i)
				- Math.floor(Math.floor(i / 28)
						* Math.floor(1 - Math.floor(i / 28))
						* Math.floor(29 / Math.floor(i + 1))
						* Math.floor(Math.floor(21 - n) / 11));
		j = Math.floor(year) + Math.floor(year / 4) + Math.floor(i) + 2
				- Math.floor(c) + Math.floor(c / 4);
		j = Math.floor(j) - Math.floor(7 * Math.floor(j / 7));
		l = Math.floor(i) - Math.floor(j);
		m = 3 + Math.floor(Math.floor(l + 40) / 44);
		d = l + 28 - (31 * Math.floor(m / 4));

        // Atualizamos as datas da páscoa
		int diaPascoa = (int) d;
        int mesPascoa = (int) m - 1;
        
        Calendar auxCalendar = Calendar.getInstance();
        //pascoa
        auxCalendar.set(year, mesPascoa, diaPascoa);
        if (CompareCalendar(auxCalendar, date)){
            return true;
        }
        //segunda de carnaval
        auxCalendar.add(Calendar.DAY_OF_MONTH, -48);
        if (CompareCalendar(auxCalendar, date)){
            return true;
        }
        //terca de carnaval
        auxCalendar.add(Calendar.DAY_OF_MONTH, 1);
        if (CompareCalendar(auxCalendar, date)){
            return true;
        }
        //sexta-feira santa
        auxCalendar.set(year, mesPascoa, diaPascoa - 2);
        if (CompareCalendar(auxCalendar, date)){
            return true;
        }
        //Corpus Christi
        auxCalendar.add(Calendar.DAY_OF_MONTH, 62);
        if (CompareCalendar(auxCalendar, date)){
            return true;
        }
        return false;
    }
    
    public static Boolean IsCalculatedHolidayStatic(Calendar date){
        
		// Feriados móveis
		// 9: Segunda Carnaval (48 dias antes da Páscoa)
		// 10: Terca Carnaval (1 dia apos segunda de carnaval)
		// 11: Sexta Feira Santa (02 dias antes da Páscoa)
		// 12: Corpus Christi (60 dias apos a Páscoa)

        // A data da pácoa é data base para cálculo dos outros feriados móveis.
        
        int year = date.get(Calendar.YEAR);

		// Algoritmo de Gauss
		double c, n, m, k, i, j, l, d;

		c = year / 100;
		c = year / 100;
		n = year - Math.floor(19 * Math.floor(year / 19));
		k = Math.floor((c - 17) / 25);
		i = Math.floor(c) - Math.floor(c / 4) - Math.floor((c - k) / 3)
				+ Math.floor(19 * n) + 15;
		i = Math.floor(i) - Math.floor(30 * Math.floor(i / 30));
		i = Math.floor(i)
				- Math.floor(Math.floor(i / 28)
						* Math.floor(1 - Math.floor(i / 28))
						* Math.floor(29 / Math.floor(i + 1))
						* Math.floor(Math.floor(21 - n) / 11));
		j = Math.floor(year) + Math.floor(year / 4) + Math.floor(i) + 2
				- Math.floor(c) + Math.floor(c / 4);
		j = Math.floor(j) - Math.floor(7 * Math.floor(j / 7));
		l = Math.floor(i) - Math.floor(j);
		m = 3 + Math.floor(Math.floor(l + 40) / 44);
		d = l + 28 - (31 * Math.floor(m / 4));

        // Atualizamos as datas da páscoa
		int diaPascoa = (int) d;
        int mesPascoa = (int) m - 1;
        
        Calendar auxCalendar = Calendar.getInstance();
        //pascoa
        auxCalendar.set(year, mesPascoa, diaPascoa);
        if (CompareCalendarStatic(auxCalendar, date)){
            return true;
        }
        //segunda de carnaval
        auxCalendar.add(Calendar.DAY_OF_MONTH, -48);
        if (CompareCalendarStatic(auxCalendar, date)){
            return true;
        }
        //terca de carnaval
        auxCalendar.add(Calendar.DAY_OF_MONTH, 1);
        if (CompareCalendarStatic(auxCalendar, date)){
            return true;
        }
        //sexta-feira santa
        auxCalendar.set(year, mesPascoa, diaPascoa - 2);
        if (CompareCalendarStatic(auxCalendar, date)){
            return true;
        }
        //Corpus Christi
        auxCalendar.add(Calendar.DAY_OF_MONTH, 62);
        if (CompareCalendarStatic(auxCalendar, date)){
            return true;
        }
        return false;
    }
    
    public Boolean CompareCalendar(Calendar cal1, Calendar cal2){
        if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
            && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
            && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)){
                return true;
            }
        return false;
    }
    public static Boolean CompareCalendarStatic(Calendar cal1, Calendar cal2){
        if(cal1.get(Calendar.YEAR) == cal2.get(Calendar.YEAR)
            && cal1.get(Calendar.MONTH) == cal2.get(Calendar.MONTH)
            && cal1.get(Calendar.DAY_OF_MONTH) == cal2.get(Calendar.DAY_OF_MONTH)){
                return true;
            }
        return false;
    }
    public Calendar GetLastWorkDay(Calendar date){
        Calendar retDate = (Calendar) date.clone();
        while (!isWorkDay(retDate)){
            retDate.add(Calendar.DAY_OF_MONTH, -1);;
        }
        return retDate;
    }
    public Calendar GetLastWorksDay(Calendar date, int days){
        Calendar retDate = (Calendar) date.clone();
	    retDate.add(Calendar.DAY_OF_MONTH, days);    
        while (!isWorkDay(retDate)){
	            retDate.add(Calendar.DAY_OF_MONTH, -1);;
	    }        
        return retDate;
    }
    public Calendar GetMonthEndBetween(Calendar startDate, Calendar endDate){
        Calendar workingCal = (Calendar) startDate.clone();
        while (!CompareCalendar(workingCal, endDate)){
            if (workingCal.get(Calendar.DAY_OF_MONTH) == workingCal.getActualMaximum(Calendar.DAY_OF_MONTH)){
                return workingCal;
            }
            workingCal.add(Calendar.DAY_OF_MONTH, 1);
        }
        return null;
    }
    
    public Date subtractDays(Date date, int days) {
		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.add(Calendar.DATE, -days);
				
		return cal.getTime();
	}

	public List<Date> getMonthAndLastMonth(Date dataAtual) {
		List<Date> datas = new ArrayList<>();
		for (int i = 0; i < 31; i++) {
			datas.add(subtractDays(dataAtual, 30 - i));
		}		
		
		return datas;
	}
}
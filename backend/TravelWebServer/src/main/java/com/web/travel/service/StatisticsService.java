package com.web.travel.service;

import com.web.travel.dto.ResDTO;
import com.web.travel.dto.request.admin.statistic.ProfitStatisticsResDTO;
import com.web.travel.dto.request.admin.statistic.WeekStatisticDTO;
import com.web.travel.model.enums.EOrderStatus;
import com.web.travel.repository.OrderRepository;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class StatisticsService {
    @Autowired
    OrderRepository orderRepository;
    public ResDTO getStatistics(){
        List<Date> lastDaysOfWeek = getLastWeekDays();
        List<Date> datesFromMondayToToday = getDatesFromMondayToToday();

        List<Long> profitOfLastWeek = new ArrayList<>();
        List<Long> profitOfThisWeek = new ArrayList<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

        lastDaysOfWeek.forEach(date -> {
            String[] dateStr = dateFormat.format(date).split("-");
            Double profit = orderRepository.findSumProfitByOrderDate(dateStr[0], dateStr[1], dateStr[2], EOrderStatus.STATUS_ORDERED);
            profitOfLastWeek.add(profit != null ? Math.round(profit) : 0L);
        });
        datesFromMondayToToday.forEach(date -> {
            if(date != null){
                String[] dateStr = dateFormat.format(date).split("-");
                Double profit = orderRepository.findSumProfitByOrderDate(dateStr[0], dateStr[1], dateStr[2], EOrderStatus.STATUS_ORDERED);
                profitOfThisWeek.add(profit != null ? Math.round(profit) : 0L);
            }
            else{
                profitOfThisWeek.add(0L);
            }
        });

        ProfitStatisticsResDTO response =  new ProfitStatisticsResDTO();
        WeekStatisticDTO weekStatisticDTO = new WeekStatisticDTO();
        weekStatisticDTO.setLastWeek(profitOfLastWeek);
        weekStatisticDTO.setThisWeek(profitOfThisWeek);

        response.setStatistic(weekStatisticDTO);

        Date date = new Date();
        String[] currentDateStrSplit = dateFormat.format(date).split("-");
        Double monthProfit = orderRepository.findSumProfitByMonth(String.valueOf(currentDateStrSplit[1]), EOrderStatus.STATUS_ORDERED);
        Integer orderQuantity = orderRepository.findOrderQuantityByMonth(String.valueOf(currentDateStrSplit[1]));
        Integer customQuantity = orderRepository.findCustomerQuantityByMonth(String.valueOf(currentDateStrSplit[1]));
        Double averageMonthProfit = orderRepository.findAvgProfitByMonth(String.valueOf(currentDateStrSplit[1]), EOrderStatus.STATUS_ORDERED);

        response.setProfit(monthProfit != null ? Math.round(monthProfit) : 0);
        response.setOrderQuantity(orderQuantity != null ? orderQuantity : 0);
        response.setCustomerQuantity(customQuantity != null ? customQuantity : 0);
        response.setMonthAverage(averageMonthProfit != null ? Math.round(averageMonthProfit) : 0);

        return new ResDTO(
                HttpServletResponse.SC_OK,
                true,
                "Get statistics successfully",
                response
        );
    }

    private List<Date> getLastWeekDays() {
        LocalDate today = LocalDate.now();
        LocalDate lastWeekMonday = today.minusWeeks(1).with(DayOfWeek.MONDAY);
        LocalDate lastWeekSunday = today.minusWeeks(1).with(DayOfWeek.SUNDAY);

        List<LocalDate> datesLastWeek = new ArrayList<>();

        LocalDate localDate = lastWeekMonday;
        while (!localDate.isAfter(lastWeekSunday)) {
            datesLastWeek.add(localDate);
            localDate = localDate.plusDays(1);
        }

        List<Date> dates = new ArrayList<>();
        for (LocalDate d : datesLastWeek) {
            Date date = Date.from(d.atStartOfDay(ZoneId.systemDefault()).toInstant());
            dates.add(date);
        }

        return dates;
    }

    private List<Date> getDatesFromMondayToToday(){
        LocalDate today = LocalDate.now();
        LocalDate mondayOfCurrentWeek = today.with(DayOfWeek.MONDAY);

        List<LocalDate> datesFromMondayToCurrent = new ArrayList<>();

        LocalDate date = mondayOfCurrentWeek;
        while (!date.isAfter(today)) {
            datesFromMondayToCurrent.add(date);
            date = date.plusDays(1);
        }

        List<Date> dates = new ArrayList<>();

        for(int i = 0; i < 7; i++){
            if(i < datesFromMondayToCurrent.size()){
                Date each = Date.from(datesFromMondayToCurrent.get(i).atStartOfDay(ZoneId.systemDefault()).toInstant());
                dates.add(each);
            }else{
                dates.add(null);
            }
        }

        return dates;
    }
}

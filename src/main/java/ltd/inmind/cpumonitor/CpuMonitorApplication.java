package ltd.inmind.cpumonitor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import oshi.SystemInfo;
import oshi.hardware.CentralProcessor;

import java.io.IOException;
import java.text.DecimalFormat;

@SpringBootApplication
@EnableScheduling
public class CpuMonitorApplication {

    static Logger log = LoggerFactory.getLogger(CpuMonitorApplication.class);

    final SystemInfo systemInfo = new SystemInfo();
    final CentralProcessor processor = systemInfo.getHardware().getProcessor();


    public static void main(String[] args) throws IOException {
        SpringApplication.run(CpuMonitorApplication.class, args);
        System.in.read();
    }

    @Scheduled(fixedRate = 3000)
    public void cpuRatio() throws InterruptedException {
        final long[] prevTicks = processor.getSystemCpuLoadTicks();
        Thread.sleep(300);
        final long[] ticks = processor.getSystemCpuLoadTicks();
        long nice = ticks[CentralProcessor.TickType.NICE.getIndex()] - prevTicks[CentralProcessor.TickType.NICE.getIndex()];
        long irq = ticks[CentralProcessor.TickType.IRQ.getIndex()] - prevTicks[CentralProcessor.TickType.IRQ.getIndex()];
        long softirq = ticks[CentralProcessor.TickType.SOFTIRQ.getIndex()] - prevTicks[CentralProcessor.TickType.SOFTIRQ.getIndex()];
        long steal = ticks[CentralProcessor.TickType.STEAL.getIndex()] - prevTicks[CentralProcessor.TickType.STEAL.getIndex()];
        long cSys = ticks[CentralProcessor.TickType.SYSTEM.getIndex()] - prevTicks[CentralProcessor.TickType.SYSTEM.getIndex()];
        long user = ticks[CentralProcessor.TickType.USER.getIndex()] - prevTicks[CentralProcessor.TickType.USER.getIndex()];
        long iowait = ticks[CentralProcessor.TickType.IOWAIT.getIndex()] - prevTicks[CentralProcessor.TickType.IOWAIT.getIndex()];
        long idle = ticks[CentralProcessor.TickType.IDLE.getIndex()] - prevTicks[CentralProcessor.TickType.IDLE.getIndex()];
        long totalCpu = user + nice + cSys + idle + iowait + irq + softirq + steal;
        // System.out.printf("CPU cores = %s, CPU load =%s %n", processor.getLogicalProcessorCount(), new DecimalFormat("#.##%").format(1.0 - (idle * 1.0 / totalCpu)));
        log.info("CPU cores = {}, CPU load = {}", processor.getLogicalProcessorCount(), new DecimalFormat("#.##%").format(1.0 - (idle * 1.0 / totalCpu)));
    }


}

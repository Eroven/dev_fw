package me.zhaotb.app.imp;

import me.zhaotb.app.api.MicroConf;
import me.zhaotb.app.api.CliApplication;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

@MicroConf(name = "cliAppDemo")
public class CliMicroAppDemo extends AbstractMicroAppService implements CliApplication {

    @Override
    public Options getOptions() {
        Options options = new Options();
        Option op = new Option("say",true,"说");
        op.setRequired(true);
        options.addOption(op);
        return options;
    }

    @Override
    public void doTask(CommandLine cmd, String[] args) {
        System.out.println(Arrays.toString(args));
        while(isRunning()){
            System.out.println(cmd.getOptionValue("say"));
            execute();
        }
    }

    @Override
    public void execute() {
        sleep(1, TimeUnit.SECONDS);
    }
}

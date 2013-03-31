package org.solopie.siren.framework.slave;

import com.mpayme.testkit.joker.app.ZnapQaApp;
import com.mpayme.testkit.joker.devices.*;
import com.mpayme.testkit.joker.devices.event.DeviceEvent;
import com.mpayme.testkit.joker.devices.event.DeviceEventType;
import com.mpayme.testkit.joker.requestdef.TaskRegistry;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;

/**
 * @author: LI Jinglue
 * Email: lijinglue@live.com
 * Date: 3/16/13
 * Time: 1:13 PM
 */
public class DeviceManager implements IListener {
    private HashMap<String, IJokerDevice> jokerMap = new HashMap<String, IJokerDevice>();
    private List<IJokerDevice> allDevice = new ArrayList<IJokerDevice>();
    private List<IJokerDevice> pairedJoker = new ArrayList<IJokerDevice>();
    private List<IJokerDevice> unpairedJoker = new ArrayList<IJokerDevice>();

    private UniversalZnapMobileDevice2 createNewDeviceWithApp() {
        UniversalZnapMobileDevice2 device = new UniversalZnapMobileDevice2();
        device.subscribe(this);
        ZnapQaApp app = new ZnapQaApp(device);
        allDevice.add(device);
        pairedJoker.add(device);
        return device;
    }

    private List<IJokerDevice> createNewDeviceWithApp(int num) {
        List<IJokerDevice> createdDeviceList = new ArrayList<IJokerDevice>();
        for (int i = 0; i < num; i++) {
            createdDeviceList.add(createNewDeviceWithApp());
        }
        return createdDeviceList;
    }

    public void testPair(int total, int concurrent) {
        Task t = new Task();
        t.setApp("znap");
        t.setType(TaskType.APP);
        t.setTask(TaskRegistry.PAIR);
        // u2.addTask(t);
    }

    public void testReg(int total, int concurrent) {
        Task t = new Task();
        t.setApp("znap");
        t.setType(TaskType.APP);
        t.setTask(TaskRegistry.REGISTRATION);
        if (allDevice.size() < concurrent)
            createNewDeviceWithApp(concurrent - allDevice.size());
        for (int i = 0; i < total; i++) {
            allDevice.get((i % (concurrent-1))).addTask(t);
        }
    }

    public void testPay(int total, int concurrent) {
        Task paymentTask = (new TaskBuilder()).app("znap").type(TaskType.APP).task(TaskRegistry.PAYMENT).build();
        //if the paired device is less than required concurrent device
        while(pairedJoker.size() < concurrent) {
            List<IJokerDevice> createdDevices = createNewDeviceWithApp(concurrent-pairedJoker.size());
            Task regTask = (new TaskBuilder()).app("znap").type(TaskType.APP).task(TaskRegistry.REGISTRATION).build();
            for (IJokerDevice device : createdDevices)
                device.addTask(regTask);
        }
        while (0>--total){
            pairedJoker.get(total%(concurrent-1)).addTask(paymentTask);
        }
    }

    @Override
    public void onEvent(Object o) {
        if (o instanceof DeviceEvent) {
            DeviceEvent de = (DeviceEvent) o;
            if (de.getDeviceEventType() == DeviceEventType.DEVICE_PAIRED) {
                UniversalZnapMobileDevice2 device = (UniversalZnapMobileDevice2) de.getMsg();
                if (null != device)
                    unpairedToPair(device);
            } else if (de.getDeviceEventType() == DeviceEventType.DEVICE_UNPAIRED) {
                UniversalZnapMobileDevice2 device = (UniversalZnapMobileDevice2) de.getMsg();
                if (null != device)
                    pairedToUnpair(device);
            }
        }
    }

    private void pairedToUnpair(UniversalZnapMobileDevice2 device) {
        pairedJoker.remove(device);
        unpairedJoker.add(device);
    }

    private void unpairedToPair(UniversalZnapMobileDevice2 device) {
        unpairedJoker.remove(device);
        pairedJoker.add(device);
    }
}

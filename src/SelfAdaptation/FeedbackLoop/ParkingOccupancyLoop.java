package SelfAdaptation.FeedbackLoop;

import IotDomain.*;
import SelfAdaptation.Instrumentation.FeedbackLoopGatewayBuffer;

import java.util.HashMap;
import java.util.LinkedList;

public class ParkingOccupancyLoop extends GenericFeedbackLoop {
    private QualityOfService qualityOfService;
    private HashMap<Mote, LinkedList<Double>> reliableMinPowerBuffers;
    private FeedbackLoopGatewayBuffer gatewayBuffer;
    /**
     * Constructs a new instance of the signal based adaptation approach with a given quality of service.
     *
     * @param qualityOfService The quality of service for the received signal strength.
     */

    public ParkingOccupancyLoop(QualityOfService qualityOfService) {
        super("Parking-occupancy-based");
        this.qualityOfService = qualityOfService;
        gatewayBuffer = new FeedbackLoopGatewayBuffer();
        reliableMinPowerBuffers = new HashMap<>();
    }

    private HashMap<Mote,LinkedList<Double>> getReliableMinPowerBuffers(){
        return this.reliableMinPowerBuffers;
    }

    private void putReliableMinPowerBuffer(Mote mote, LinkedList<Double> reliableMinPowerBuffer){
        this.reliableMinPowerBuffers.put(mote,reliableMinPowerBuffer);
    }

    /**
     * returns a map with gateway buffers.
     * @return A map with gateway buffers.
     */
    private FeedbackLoopGatewayBuffer getGatewayBuffer() {
        return gatewayBuffer;
    }

    @Override
    public void adapt(Mote mote, Gateway dataGateway) {
        /**
         First we check if we have received the message already from all gateways.
         */
        getGatewayBuffer().add(mote, dataGateway);
        if (getGatewayBuffer().hasReceivedAllSignals(mote)) {
            /**
             * check what is the highest received signal strength.
             */
            LinkedList<LoraTransmission> receivedSignals = getGatewayBuffer().getReceivedSignals(mote);

            Double receivedPower = receivedSignals.getFirst().getTransmissionPower();

            for (LoraTransmission transmission : receivedSignals) {
                if (receivedPower < transmission.getTransmissionPower()) {
                    receivedPower = transmission.getTransmissionPower();
                }
            }

            // Adapt power of the mote, as per the parking-occupancy logic of
            // adapting power with change in parking occupancy over a factor of 7 in any of the parking locations
            int total_vehicles = mote.getParkingOccupancy();
            int mote_power = (int) Math.floor((double)total_vehicles / 7);
            if (total_vehicles % 7 != 0 && total_vehicles != 0)
                mote_power = mote_power + 1;
            mote.setTransmissionPower(mote_power);
            getMoteEffector().setPower(mote, mote_power);

            /**
             * If the buffer has an entry for the current mote, the new highest received signal strength is added to it,
             * else a new buffer is created and added to which we can add the signal strength.
             */
            LinkedList<Double> reliableMinPowerBuffer = new LinkedList<>();
            if (getReliableMinPowerBuffers().keySet().contains(mote)) {
                reliableMinPowerBuffer = getReliableMinPowerBuffers().get(mote);
            }
            reliableMinPowerBuffer.add((double) mote_power);
            putReliableMinPowerBuffer(mote, reliableMinPowerBuffer);

            putReliableMinPowerBuffer(mote, new LinkedList<>());

        }
    }
}
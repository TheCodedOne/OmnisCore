package me.jezza.oc.tests;

import me.jezza.oc.api.NetworkResponse;
import me.jezza.oc.api.interfaces.IMessageProcessor;
import me.jezza.oc.api.interfaces.INetworkMessage;
import me.jezza.oc.api.interfaces.INetworkNode;
import me.jezza.oc.common.core.CoreProperties;

import java.util.Collection;
import java.util.HashSet;

public class TestNode implements INetworkNode {

    private HashSet<INetworkNode> nearbyNodes;
    private IMessageProcessor messageProcessor;

    public TestNode() {
        nearbyNodes = new HashSet<>();
    }

    @Override
    public NetworkResponse.Override onMessagePosted(INetworkMessage message) {
        CoreProperties.logger.info("Ignoring...");
        return NetworkResponse.Override.IGNORE;
    }

    @Override
    public NetworkResponse.Override onMessageReceived(INetworkMessage message) {
        CoreProperties.logger.info("On message received...");
        return NetworkResponse.Override.IGNORE;
    }

    @Override
    public NetworkResponse.MessageResponse onMessageComplete(INetworkMessage message) {
        CoreProperties.logger.info("On message complete...");
        return NetworkResponse.MessageResponse.VALID;
    }

    @Override
    public Collection<INetworkNode> getNearbyNodes() {
        CoreProperties.logger.info("Getting all nearby nodes.");
        return nearbyNodes;
    }

    @Override
    public void setNetworkCore(IMessageProcessor messageProcessor) {
        CoreProperties.logger.info("Setting network core.");
        this.messageProcessor = messageProcessor;
    }

    @Override
    public IMessageProcessor getNetworkCore() {
        return messageProcessor;
    }

    public void addNearbyNode(INetworkNode node) {
        if (!nearbyNodes.contains(node))
            nearbyNodes.add(node);
    }

    public void removeNearbyNode(INetworkNode node) {
        if (nearbyNodes.contains(node))
            nearbyNodes.remove(node);
    }

}

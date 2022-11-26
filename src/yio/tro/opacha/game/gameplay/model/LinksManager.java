package yio.tro.opacha.game.gameplay.model;

import yio.tro.opacha.game.export_import.SavableYio;
import yio.tro.opacha.game.gameplay.IGameplayManager;
import yio.tro.opacha.game.gameplay.ObjectsLayer;

import java.util.ArrayList;

public class LinksManager implements IGameplayManager, SavableYio{

    ObjectsLayer objectsLayer;
    public ArrayList<Link> links;


    public LinksManager(ObjectsLayer objectsLayer) {
        this.objectsLayer = objectsLayer;
        links = new ArrayList<>();
    }


    @Override
    public void defaultValues() {
        clear();
    }


    public void clear() {
        links.clear();
    }


    public Link addLink(Planet one, Planet two) {
        Link freshLink = new Link(this);
        freshLink.setFractionType(FractionType.neutral);
        freshLink.setOne(one);
        freshLink.setTwo(two);
        freshLink.updateMetrics();
        one.onAdjoinedLinkAdded(freshLink);
        two.onAdjoinedLinkAdded(freshLink);
        links.add(freshLink);
        return freshLink;
    }


    public void removeLink(Planet one, Planet two) {
        Link link = getLink(one, two);
        if (link == null) return;
        removeLink(link);
    }


    public void removeLink(Link link) {
        link.one.onAdjoinedLinkRemoved(link);
        link.two.onAdjoinedLinkRemoved(link);
        links.remove(link);
    }


    public Link getLink(Planet one, Planet two) {
        for (Link link : links) {
            if (!link.equals(one, two)) continue;
            return link;
        }
        return null;
    }


    public void syncFractions() {
        for (Link link : links) {
            FractionType targetFractionType = getTargetFractionType(link);
            if (targetFractionType == link.fractionType) continue;
            applyFractionChange(link, targetFractionType);
        }
    }


    private void applyFractionChange(Link link, FractionType fractionType) {
        FractionType previousFractionType = link.fractionType;
        link.setFractionType(fractionType);
        objectsLayer.particlesManager.spawnSomeParticlesOnLink(link, previousFractionType);
    }


    private FractionType getTargetFractionType(Link link) {
        if (link.one.fraction == link.two.fraction) return link.one.fraction;
        return FractionType.neutral;
    }


    @Override
    public void onEndCreation() {
        for (Link link : links) {
            link.onEndCreation();
        }
    }


    @Override
    public void moveActually() {
        moveLinksActually();
    }


    private void moveLinksActually() {
        for (Link link : links) {
            link.moveActually();
        }
    }


    @Override
    public void moveVisually() {
        moveLinksVisually();
    }


    private void moveLinksVisually() {
        for (Link link : links) {
            link.moveVisually();
        }
    }


    @Override
    public String saveToString() {
        StringBuilder builder = new StringBuilder();
        for (Link link : links) {
            builder.append(link.saveToString()).append(",");
        }
        return builder.toString();
    }
}

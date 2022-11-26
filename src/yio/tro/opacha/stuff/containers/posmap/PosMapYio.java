package yio.tro.opacha.stuff.containers.posmap;

import yio.tro.opacha.stuff.PointYio;
import yio.tro.opacha.stuff.RectangleYio;

import java.util.ArrayList;
import java.util.ListIterator;


public class PosMapYio {

    public int width, height;
    public float sectorSize;
    private ArrayList<PosMapObjectYio> matrix[][];
    public RectangleYio mapPos;


    public PosMapYio(RectangleYio mapPos, double sectorSize) {
        this.mapPos = mapPos;
        this.sectorSize = (float) sectorSize;
        width = (int)(mapPos.width / sectorSize) + 2;
        height = (int)(mapPos.height / sectorSize) + 2;
        matrix = new ArrayList[width][height];
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                matrix[i][j] = new ArrayList<>();
            }
        }
    }


    public void updateObjectPos(PosMapObjectYio objectYio) {
        objectYio.updatePosMapPosition();
        updateCurrentIndexPoint(objectYio);
        if (    !indexPointsAreEqual(objectYio.indexPoint, objectYio.lastIndexPoint) &&
                isIndexPointInsideLevel(objectYio.indexPoint)) {
            removeObjectFromSector(objectYio, objectYio.lastIndexPoint);
            addObjectToSector(objectYio, objectYio.indexPoint);
            objectYio.lastIndexPoint.setBy(objectYio.indexPoint);
        }
    }


    private boolean isIndexPointInsideLevel(PmSectorIndex sectorIndex) {
        if (sectorIndex.x < 0) return false;
        if (sectorIndex.x >= width) return false;
        if (sectorIndex.y < 0) return false;
        if (sectorIndex.y >= height) return false;
        return true;
    }


    private void updateLastIndexPoint(PosMapObjectYio objectYio) {
        transformCoorToIndex(objectYio.posMapPosition.x, objectYio.posMapPosition.y, objectYio.lastIndexPoint);
    }


    private void updateCurrentIndexPoint(PosMapObjectYio objectYio) {
        transformCoorToIndex(objectYio.posMapPosition.x, objectYio.posMapPosition.y, objectYio.indexPoint);
    }


    public void removeObject(PosMapObjectYio objectYio) {
        removeObjectFromSector(objectYio, objectYio.lastIndexPoint);
    }


    private void removeObjectFromSector(PosMapObjectYio objectYio, PmSectorIndex sectorIndex) {
        ListIterator iterator = matrix[sectorIndex.x][sectorIndex.y].listIterator();
        while (iterator.hasNext()) {
            PosMapObjectYio posMapObjectYio = (PosMapObjectYio) iterator.next();
            if (posMapObjectYio == objectYio) {
                iterator.remove();
                return;
            }
        }
    }


    private void addObjectToSector(PosMapObjectYio objectYio, PmSectorIndex sectorIndex) {
        ListIterator iterator = matrix[sectorIndex.x][sectorIndex.y].listIterator();
        iterator.add(objectYio);
    }


    private boolean indexPointsAreEqual(PmSectorIndex p1, PmSectorIndex p2) {
        return p1.x == p2.x && p1.y == p2.y;
    }


    public void addObject(PosMapObjectYio object) {
        object.updatePosMapPosition();
        updateLastIndexPoint(object);
        addObjectToSector(object, object.lastIndexPoint);
    }


    public void transformCoorToIndex(double x, double y, PmSectorIndex indexPoint) {
        indexPoint.x = (int)((x - mapPos.x) / sectorSize);
        indexPoint.y = (int)((y - mapPos.y) / sectorSize);
    }


    public void transformCoorToIndex(PointYio position, PmSectorIndex sectorIndex) {
        transformCoorToIndex(position.x, position.y, sectorIndex);
    }


    private boolean isCoorInside(int i, int j) {
        return i >= 0 && i < width && j >= 0 && j < height;
    }


    public void clear() {
        for (int i = 0; i < width; i++) {
            for (int j = 0; j < height; j++) {
                matrix[i][j].clear();
            }
        }
    }


    public void showInConsole() {
        for (int j = 0; j < height; j++) {
            for (int i = 0; i < width; i++) {
                System.out.print(matrix[i][j].size() + " ");
            }
            System.out.println("");
        }
        System.out.println("");
    }


    public ArrayList<PosMapObjectYio> getSectorByPos(double x, double y) {
        int index_x = (int)((x - mapPos.x) / sectorSize);
        int index_y = (int)((y - mapPos.y) / sectorSize);
        return getSector(index_x, index_y);
    }


    public ArrayList<PosMapObjectYio> getSector(int i, int j) {
        if (!isCoorInside(i, j)) return null;
        return matrix[i][j];
    }
}

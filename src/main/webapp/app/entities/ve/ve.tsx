import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './ve.reducer';

export const Ve = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const veList = useAppSelector(state => state.ve.entities);
  const loading = useAppSelector(state => state.ve.loading);

  const getAllEntities = () => {
    dispatch(
      getEntities({
        sort: `${sortState.sort},${sortState.order}`,
      }),
    );
  };

  const sortEntities = () => {
    getAllEntities();
    const endURL = `?sort=${sortState.sort},${sortState.order}`;
    if (pageLocation.search !== endURL) {
      navigate(`${pageLocation.pathname}${endURL}`);
    }
  };

  useEffect(() => {
    sortEntities();
  }, [sortState.order, sortState.sort]);

  const sort = p => () => {
    setSortState({
      ...sortState,
      order: sortState.order === ASC ? DESC : ASC,
      sort: p,
    });
  };

  const handleSyncList = () => {
    sortEntities();
  };

  const getSortIconByFieldName = (fieldName: string) => {
    const sortFieldName = sortState.sort;
    const order = sortState.order;
    if (sortFieldName !== fieldName) {
      return faSort;
    } else {
      return order === ASC ? faSortUp : faSortDown;
    }
  };

  return (
    <div>
      <h2 id="ve-heading" data-cy="VeHeading">
        <Translate contentKey="projectMovieApp.ve.home.title">Ves</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="projectMovieApp.ve.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/ve/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="projectMovieApp.ve.home.createLabel">Create new Ve</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {veList && veList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="projectMovieApp.ve.id">ID</Translate> <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('soDienThoai')}>
                  <Translate contentKey="projectMovieApp.ve.soDienThoai">So Dien Thoai</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('soDienThoai')} />
                </th>
                <th className="hand" onClick={sort('email')}>
                  <Translate contentKey="projectMovieApp.ve.email">Email</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('email')} />
                </th>
                <th className="hand" onClick={sort('giaTien')}>
                  <Translate contentKey="projectMovieApp.ve.giaTien">Gia Tien</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('giaTien')} />
                </th>
                <th className="hand" onClick={sort('tinhTrang')}>
                  <Translate contentKey="projectMovieApp.ve.tinhTrang">Tinh Trang</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('tinhTrang')} />
                </th>
                <th>
                  <Translate contentKey="projectMovieApp.ve.suatChieu">Suat Chieu</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {veList.map((ve, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/ve/${ve.id}`} color="link" size="sm">
                      {ve.id}
                    </Button>
                  </td>
                  <td>{ve.soDienThoai}</td>
                  <td>{ve.email}</td>
                  <td>{ve.giaTien}</td>
                  <td>
                    <Translate contentKey={`projectMovieApp.TinhTrangVe.${ve.tinhTrang}`} />
                  </td>
                  <td>{ve.suatChieu ? <Link to={`/suat-chieu/${ve.suatChieu.id}`}>{ve.suatChieu.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/ve/${ve.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/ve/${ve.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/ve/${ve.id}/delete`)}
                        color="danger"
                        size="sm"
                        data-cy="entityDeleteButton"
                      >
                        <FontAwesomeIcon icon="trash" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.delete">Delete</Translate>
                        </span>
                      </Button>
                    </div>
                  </td>
                </tr>
              ))}
            </tbody>
          </Table>
        ) : (
          !loading && (
            <div className="alert alert-warning">
              <Translate contentKey="projectMovieApp.ve.home.notFound">No Ves found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default Ve;

import React, { useState, useEffect } from 'react';
import { Link, useLocation, useNavigate } from 'react-router-dom';
import { Button, Table } from 'reactstrap';
import { Translate, TextFormat, getSortState } from 'react-jhipster';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSort, faSortUp, faSortDown } from '@fortawesome/free-solid-svg-icons';
import { APP_DATE_FORMAT, APP_LOCAL_DATE_FORMAT } from 'app/config/constants';
import { ASC, DESC, SORT } from 'app/shared/util/pagination.constants';
import { overrideSortStateWithQueryParams } from 'app/shared/util/entity-utils';
import { useAppDispatch, useAppSelector } from 'app/config/store';

import { getEntities } from './suat-chieu.reducer';

export const SuatChieu = () => {
  const dispatch = useAppDispatch();

  const pageLocation = useLocation();
  const navigate = useNavigate();

  const [sortState, setSortState] = useState(overrideSortStateWithQueryParams(getSortState(pageLocation, 'id'), pageLocation.search));

  const suatChieuList = useAppSelector(state => state.suatChieu.entities);
  const loading = useAppSelector(state => state.suatChieu.loading);

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
      <h2 id="suat-chieu-heading" data-cy="SuatChieuHeading">
        <Translate contentKey="projectMovieApp.suatChieu.home.title">Suat Chieus</Translate>
        <div className="d-flex justify-content-end">
          <Button className="me-2" color="info" onClick={handleSyncList} disabled={loading}>
            <FontAwesomeIcon icon="sync" spin={loading} />{' '}
            <Translate contentKey="projectMovieApp.suatChieu.home.refreshListLabel">Refresh List</Translate>
          </Button>
          <Link to="/suat-chieu/new" className="btn btn-primary jh-create-entity" id="jh-create-entity" data-cy="entityCreateButton">
            <FontAwesomeIcon icon="plus" />
            &nbsp;
            <Translate contentKey="projectMovieApp.suatChieu.home.createLabel">Create new Suat Chieu</Translate>
          </Link>
        </div>
      </h2>
      <div className="table-responsive">
        {suatChieuList && suatChieuList.length > 0 ? (
          <Table responsive>
            <thead>
              <tr>
                <th className="hand" onClick={sort('id')}>
                  <Translate contentKey="projectMovieApp.suatChieu.id">ID</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('id')} />
                </th>
                <th className="hand" onClick={sort('ngayChieu')}>
                  <Translate contentKey="projectMovieApp.suatChieu.ngayChieu">Ngay Chieu</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('ngayChieu')} />
                </th>
                <th className="hand" onClick={sort('gioChieu')}>
                  <Translate contentKey="projectMovieApp.suatChieu.gioChieu">Gio Chieu</Translate>{' '}
                  <FontAwesomeIcon icon={getSortIconByFieldName('gioChieu')} />
                </th>
                <th>
                  <Translate contentKey="projectMovieApp.suatChieu.phong">Phong</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th>
                  <Translate contentKey="projectMovieApp.suatChieu.phim">Phim</Translate> <FontAwesomeIcon icon="sort" />
                </th>
                <th />
              </tr>
            </thead>
            <tbody>
              {suatChieuList.map((suatChieu, i) => (
                <tr key={`entity-${i}`} data-cy="entityTable">
                  <td>
                    <Button tag={Link} to={`/suat-chieu/${suatChieu.id}`} color="link" size="sm">
                      {suatChieu.id}
                    </Button>
                  </td>
                  <td>
                    {suatChieu.ngayChieu ? <TextFormat type="date" value={suatChieu.ngayChieu} format={APP_LOCAL_DATE_FORMAT} /> : null}
                  </td>
                  <td>{suatChieu.gioChieu}</td>
                  <td>{suatChieu.phong ? <Link to={`/phong/${suatChieu.phong.id}`}>{suatChieu.phong.id}</Link> : ''}</td>
                  <td>{suatChieu.phim ? <Link to={`/phim/${suatChieu.phim.id}`}>{suatChieu.phim.id}</Link> : ''}</td>
                  <td className="text-end">
                    <div className="btn-group flex-btn-group-container">
                      <Button tag={Link} to={`/suat-chieu/${suatChieu.id}`} color="info" size="sm" data-cy="entityDetailsButton">
                        <FontAwesomeIcon icon="eye" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.view">View</Translate>
                        </span>
                      </Button>
                      <Button tag={Link} to={`/suat-chieu/${suatChieu.id}/edit`} color="primary" size="sm" data-cy="entityEditButton">
                        <FontAwesomeIcon icon="pencil-alt" />{' '}
                        <span className="d-none d-md-inline">
                          <Translate contentKey="entity.action.edit">Edit</Translate>
                        </span>
                      </Button>
                      <Button
                        onClick={() => (window.location.href = `/suat-chieu/${suatChieu.id}/delete`)}
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
              <Translate contentKey="projectMovieApp.suatChieu.home.notFound">No Suat Chieus found</Translate>
            </div>
          )
        )}
      </div>
    </div>
  );
};

export default SuatChieu;
